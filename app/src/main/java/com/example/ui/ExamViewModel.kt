package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import com.example.api.queryGeminiAI
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ExamViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getInstance(application)
    private val repository = ExamRepository(database.dao)

    // --- Category & Domain State ---
    val subjects = listOf("All", "History", "Polity", "Geography", "Science", "Economy & Banking", "Static GK")
    val examCategories = listOf("SSC & Railway", "CDS Level", "NDA Level")

    private val _selectedSubject = MutableStateFlow("All")
    val selectedSubject: StateFlow<String> = _selectedSubject.asStateFlow()

    private val _selectedCategory = MutableStateFlow("SSC & Railway")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    // --- Dark Mode / Theme Settings ---
    private val _themeMode = MutableStateFlow("system") // system, light, dark
    val themeMode: StateFlow<String> = _themeMode.asStateFlow()

    fun setThemeMode(mode: String) {
        _themeMode.value = mode
    }

    // --- Target Exam Levels ---
    private val _activeExamLevel = MutableStateFlow("SSC Level") // SSC Level, NDA Level, CDS Level
    val activeExamLevel: StateFlow<String> = _activeExamLevel.asStateFlow()

    fun setExamLevel(level: String) {
        _activeExamLevel.value = level
    }

    // --- Gamified Study Streaks and Badges ---
    private val _studyStreak = MutableStateFlow(5) // 5-day default streak
    val studyStreak: StateFlow<Int> = _studyStreak.asStateFlow()

    fun incrementStreak() {
        _studyStreak.value = _studyStreak.value + 1
    }

    val unlockedBadges = listOf(
        BadgeItem("Syllabus Pioneer", "Read your first interactive study outline", "ic_award_star"),
        BadgeItem("Perfect Score", "Scored 5/5 on a mock practice drill", "ic_award_crown"),
        BadgeItem("Streak builder", "Studied for 5 or more consecutive days", "ic_award_fire"),
        BadgeItem("Defense Scholar", "Attempted any CDS or NDA specialized test", "ic_award_shield")
    )

    // --- AI Storyteller Video / Story generation state ---
    private val _isGeneratingStory = MutableStateFlow(false)
    val isGeneratingStory: StateFlow<Boolean> = _isGeneratingStory.asStateFlow()

    private val _aiLectureStory = MutableStateFlow<String?>(null)
    val aiLectureStory: StateFlow<String?> = _aiLectureStory.asStateFlow()

    fun generateStoryLecture(topicName: String, style: String, language: String) {
        _isGeneratingStory.value = true
        _aiLectureStory.value = null

        viewModelScope.launch {
            val prompt = """
                You are a charismatic, cinematic AI video lectures storytelling teacher for competitive exams like NDA, SSC, and CDS.
                Produce a highly interactive, simplified, and engaging video storytelling script for the topic: "$topicName".
                
                The user requested:
                - Style: $style (e.g., "storytelling way with visuals of fights, scenes, battles described like Battle of Panipat or French Revolution")
                - Language: $language (mix of Hindi & English is highly preferred with phonetic scripts to make it super digestible for Indian scholars).
                
                Format the answer beautifully as if it's the storyboard of an epic video lecture:
                1. [VIBRANT TITLE] & Intro
                2. [SCENE-BY-SCENE ACTION] describing the fights, the critical acts, and major historical/scientific scenes vividly so they can visualize it immediately.
                3. [SIMPLIFIED REASSURANCE] breaking down the core concepts or facts in an easy Hindi-English storytelling way.
                4. [NDA/SSC/CDS QUICK FACT CHECK] 3 core fact bullets that directly appear in these exams.
                
                Ensure the tone is exciting, warm, highly detailed, and thoroughly enjoyable.
            """.trimIndent()

            try {
                val result = queryGeminiAI(prompt)
                _aiLectureStory.value = result
            } catch (e: Exception) {
                _aiLectureStory.value = "Failed to synchronize with Gemini API. Error: ${e.message}"
            } finally {
                _isGeneratingStory.value = false
            }
        }
    }

    fun resetStory() {
        _aiLectureStory.value = null
    }

    // --- GK/GS Topics ---
    val filteredTopics = combine(
        _selectedSubject,
        _selectedCategory
    ) { subject, _ ->
        if (subject == "All") {
            repository.getTopics()
        } else {
            repository.getTopicsBySubject(subject)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- User Attempt Data & Strengths/Weaknesses Analytics ---
    val attempts: StateFlow<List<UserAttempt>> = repository.allAttempts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // UI representation of subject proficiency
    val subjectProficiency: StateFlow<Map<String, Proficiency>> = repository.allAttempts.map { attemptList ->
        val map = mutableMapOf<String, Proficiency>()
        // Initialize default profiles
        listOf("History", "Polity", "Geography", "Science", "Economy & Banking").forEach { sub ->
            map[sub] = Proficiency(sub, 0, 0)
        }
        attemptList.forEach { att ->
            val existing = map[att.subjectName] ?: Proficiency(att.subjectName, 0, 0)
            map[att.subjectName] = existing.copy(
                correct = existing.correct + att.correctCount,
                total = existing.total + att.totalCount
            )
        }
        map
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    // --- Active Lessons / Video player ---
    private val _activeTopic = MutableStateFlow<GkGsTopic?>(MockStaticData.topics.firstOrNull())
    val activeTopic: StateFlow<GkGsTopic?> = _activeTopic.asStateFlow()

    private val _isVideoPlaying = MutableStateFlow(false)
    val isVideoPlaying: StateFlow<Boolean> = _isVideoPlaying.asStateFlow()

    private val _videoPlaybackProgress = MutableStateFlow(0f)
    val videoPlaybackProgress: StateFlow<Float> = _videoPlaybackProgress.asStateFlow()

    // --- Spaced-Repetition Mistaken Questions List ---
    val mistakenQuestions: StateFlow<List<MistakenQuestion>> = repository.getAllMistakenQuestions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- Student Leaderboard ---
    val leaderboard: StateFlow<List<LeaderboardEntry>> = repository.getLeaderboard()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- Notes List ---
    val notes: StateFlow<List<UserNote>> = repository.allNotes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- Gemini AI chat workspace ---
    private val _aiChatHistory = MutableStateFlow<List<ChatMessage>>(listOf(
        ChatMessage("system", "Hello! I am EZ with AI, your virtual study companion. Ask me any GK/GS questions, or construct a rapid summary on topics of SSC, CDS, NDA or Banking exams!")
    ))
    val aiChatHistory: StateFlow<List<ChatMessage>> = _aiChatHistory.asStateFlow()

    private val _isAiLoading = MutableStateFlow(false)
    val isAiLoading: StateFlow<Boolean> = _isAiLoading.asStateFlow()

    // --- Active Quiz session State ---
    private val _quizQuestions = MutableStateFlow<List<QuizQuestion>>(emptyList())
    val quizQuestions: StateFlow<List<QuizQuestion>> = _quizQuestions.asStateFlow()

    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex: StateFlow<Int> = _currentQuestionIndex.asStateFlow()

    private val _selectedOption = MutableStateFlow<String?>(null)
    val selectedOption: StateFlow<String?> = _selectedOption.asStateFlow()

    private val _showExplanation = MutableStateFlow(false)
    val showExplanation: StateFlow<Boolean> = _showExplanation.asStateFlow()

    private val _quizCorrectCount = MutableStateFlow(0)
    val quizCorrectCount: StateFlow<Int> = _quizCorrectCount.asStateFlow()

    private val _quizFinished = MutableStateFlow(false)
    val quizFinished: StateFlow<Boolean> = _quizFinished.asStateFlow()

    private val _quizIsReviewTheme = MutableStateFlow(false) // If taking a rescue quiz for mistakes
    val quizIsReviewTheme: StateFlow<Boolean> = _quizIsReviewTheme.asStateFlow()

    // --- Settings & Notifications ---
    private val _reminderEnabled = MutableStateFlow(true)
    val reminderEnabled: StateFlow<Boolean> = _reminderEnabled.asStateFlow()

    private val _dailyGoalComplete = MutableStateFlow(false)
    val dailyGoalComplete: StateFlow<Boolean> = _dailyGoalComplete.asStateFlow()

    // --- Operations/Actions ---

    fun setSubject(subject: String) {
        _selectedSubject.value = subject
    }

    fun setCategory(category: String) {
        _selectedCategory.value = category
    }

    fun setActiveTopic(topic: GkGsTopic) {
        _activeTopic.value = topic
        _isVideoPlaying.value = false
        _videoPlaybackProgress.value = 0f
    }

    fun toggleVideoPlay() {
        _isVideoPlaying.value = !_isVideoPlaying.value
    }

    fun updateVideoPlayback(progress: Float) {
        _videoPlaybackProgress.value = progress
        if (progress >= 1f) {
            _isVideoPlaying.value = false
        }
    }

    // --- Notes ---
    fun saveNote(title: String, content: String, topicName: String) {
        viewModelScope.launch {
            repository.saveNote(title, content, topicName)
        }
    }

    fun deleteNote(id: Int) {
        viewModelScope.launch {
            repository.deleteNote(id)
        }
    }

    // --- Settings ---
    fun toggleReminder(enabled: Boolean) {
        _reminderEnabled.value = enabled
    }

    // --- AI Chat ---
    fun askGemini(prompt: String) {
        if (prompt.isBlank()) return
        val userMsg = ChatMessage("user", prompt)
        _aiChatHistory.value = _aiChatHistory.value + userMsg
        _isAiLoading.value = true

        viewModelScope.launch {
            val response = queryGeminiAI(prompt)
            _aiChatHistory.value = _aiChatHistory.value + ChatMessage("ai", response)
            _isAiLoading.value = false
        }
    }

    fun clearChat() {
        _aiChatHistory.value = listOf(
            ChatMessage("system", "History cleared. How can I help you study now?")
        )
    }

    // --- Interactive Quiz Session ---
    private val _isGeneratingQuiz = MutableStateFlow(false)
    val isGeneratingQuiz: StateFlow<Boolean> = _isGeneratingQuiz.asStateFlow()

    fun startTopicQuiz(topic: GkGsTopic) {
        viewModelScope.launch {
            _quizIsReviewTheme.value = false
            val qList = repository.getQuizForTopic(topic.name, quizSize = 5)
            _quizQuestions.value = qList
            _currentQuestionIndex.value = 0
            _selectedOption.value = null
            _showExplanation.value = false
            _quizCorrectCount.value = 0
            _quizFinished.value = false
        }
    }

    fun generateAiLevelQuiz(topic: GkGsTopic, level: String) {
        _isGeneratingQuiz.value = true
        _quizIsReviewTheme.value = false
        _quizQuestions.value = emptyList()

        viewModelScope.launch {
            val prompt = """
                You are a premium mock exam generator specializing in India's competitive exams like NDA, SSC, and CDS.
                Generate exactly 3 extremely accurate, high-fidelity mock questions on the topic: "${topic.name}".
                Adapt the style and difficulty to match the exact standards of the: "$level" level.
                
                Format exactly as shown below:
                
                [QUESTION]
                Which of the following is correct regarding ${topic.name}?
                [A] Choice option A
                [B] Choice option B
                [C] Choice option C
                [D] Choice option D
                [CORRECT]
                A
                [EXPLANATION]
                Because...
                
                (Repeat exactly this format for each of the 3 questions. Use exactly A, B, C, or D for correct answers. No extra intros or remarks.)
            """.trimIndent()

            try {
                val rawResponse = queryGeminiAI(prompt)
                val generatedQuestions = parseGeneratedQuiz(rawResponse, topic.name)
                if (generatedQuestions.isNotEmpty()) {
                    _quizQuestions.value = generatedQuestions
                    _currentQuestionIndex.value = 0
                    _selectedOption.value = null
                    _showExplanation.value = false
                    _quizCorrectCount.value = 0
                    _quizFinished.value = false
                } else {
                    // Fallback to database standard questions if parsing fails
                    startTopicQuiz(topic)
                }
            } catch (e: Exception) {
                // Failover beautifully
                startTopicQuiz(topic)
            } finally {
                _isGeneratingQuiz.value = false
            }
        }
    }

    private fun parseGeneratedQuiz(rawText: String, topicName: String): List<QuizQuestion> {
        val list = mutableListOf<QuizQuestion>()
        val blocks = rawText.split("[QUESTION]")
        var index = 1
        for (block in blocks) {
            if (block.isBlank() || !block.contains("[CORRECT]")) continue
            try {
                val qText = block.substringBefore("[A]").trim()
                val aText = block.substringAfter("[A]").substringBefore("[B]").trim()
                val bText = block.substringAfter("[B]").substringBefore("[C]").trim()
                val cText = block.substringAfter("[C]").substringBefore("[D]").trim()
                val dText = block.substringAfter("[D]").substringBefore("[CORRECT]").trim()
                val correctText = block.substringAfter("[CORRECT]").substringBefore("[EXPLANATION]").trim().uppercase()
                val expText = block.substringAfter("[EXPLANATION]").trim()

                if (qText.isNotEmpty() && aText.isNotEmpty() && correctText.isNotEmpty()) {
                    list.add(
                        QuizQuestion(
                            id = "ai_gen_${topicName.hashCode()}_$index",
                            topicName = topicName,
                            questionText = qText,
                            optionA = aText,
                            optionB = bText,
                            optionC = cText,
                            optionD = dText,
                            correctAnswer = if (correctText.length == 1) correctText else "A",
                            explanation = expText
                        )
                    )
                    index++
                }
            } catch (e: Exception) {
                // Skip faulty parsed blocks
            }
        }
        return list
    }

    /**
     * Start a Rescue Quiz composed exclusively of questions previously answered incorrectly!
     */
    fun startRescueQuiz() {
        viewModelScope.launch {
            val mistakes = repository.getAllMistakenQuestions().firstOrNull() ?: emptyList()
            if (mistakes.isNotEmpty()) {
                _quizIsReviewTheme.value = true
                val questions = mistakes.shuffled().take(5).map {
                    QuizQuestion(
                        id = it.questionId,
                        topicName = it.topicName,
                        questionText = it.questionText,
                        optionA = it.optionA,
                        optionB = it.optionB,
                        optionC = it.optionC,
                        optionD = it.optionD,
                        correctAnswer = it.correctAnswer,
                        explanation = it.explanation
                    )
                }
                _quizQuestions.value = questions
                _currentQuestionIndex.value = 0
                _selectedOption.value = null
                _showExplanation.value = false
                _quizCorrectCount.value = 0
                _quizFinished.value = false
            }
        }
    }

    fun submitAnswer(option: String) {
        if (_selectedOption.value != null) return // Already answered
        _selectedOption.value = option
        _showExplanation.value = true

        val currentQuestion = _quizQuestions.value.getOrNull(_currentQuestionIndex.value) ?: return
        val isCorrect = option.equals(currentQuestion.correctAnswer, ignoreCase = true)

        viewModelScope.launch {
            if (isCorrect) {
                _quizCorrectCount.value = _quizCorrectCount.value + 1
                // Resolve mistake if it was a previously recorded mistake!
                repository.resolveMistake(currentQuestion.id)
            } else {
                // Save mistake for Spaced-Repetition and ask same in next quiz!
                repository.recordMistake(currentQuestion)
            }
        }
    }

    fun nextQuestion() {
        val nextIdx = _currentQuestionIndex.value + 1
        if (nextIdx < _quizQuestions.value.size) {
            _currentQuestionIndex.value = nextIdx
            _selectedOption.value = null
            _showExplanation.value = false
        } else {
            finishQuiz()
        }
    }

    private fun finishQuiz() {
        val topic = _activeTopic.value
        val correct = _quizCorrectCount.value
        val total = _quizQuestions.value.size

        if (total > 0) {
            viewModelScope.launch {
                repository.saveQuizResult(
                    category = _selectedCategory.value,
                    topicName = topic?.name ?: "GK GS",
                    subjectName = topic?.subject ?: "Science",
                    correctCount = correct,
                    totalCount = total
                )
                _quizFinished.value = true
                _dailyGoalComplete.value = true // Done a quiz, completes daily goal
            }
        }
    }
}

// Support data models for the VM
data class Proficiency(
    val subjectName: String,
    val correct: Int,
    val total: Int
) {
    val percentage: Float
        get() = if (total == 0) 0f else (correct.toFloat() / total.toFloat())
}

data class ChatMessage(
    val sender: String, // system, user, ai
    val message: String
)

data class BadgeItem(
    val name: String,
    val description: String,
    val iconKey: String
)
