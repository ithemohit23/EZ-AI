package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class ExamRepository(private val dao: ExamBookDao) {

    // --- Static GK GS Topics ---
    fun getTopics(): List<GkGsTopic> = MockStaticData.topics

    fun getTopicsBySubject(subject: String): List<GkGsTopic> {
        return MockStaticData.topics.filter { it.subject.equals(subject, ignoreCase = true) }
    }

    fun getTopicById(id: String): GkGsTopic? {
        return MockStaticData.topics.find { it.id == id }
    }

    // --- Quizzes and Mistake Repetition ---
    fun getQuestionsByTopic(topicName: String): List<QuizQuestion> {
        return MockStaticData.questions.filter { it.topicName.equals(topicName, ignoreCase = true) }
    }

    /**
     * Prepares a highly interactive quiz for a topic.
     * It fetches mistakes from the local SQLite database first.
     * It then fills the remainder with standard questions up to a desired size (e.g., 5).
     * This directly implements the "repeat missed questions in future quizzes" rule!
     */
    suspend fun getQuizForTopic(topicName: String, quizSize: Int = 5): List<QuizQuestion> {
        val mistakes = dao.getMistakenQuestionsForTopic(topicName)
        val standardQuestions = getQuestionsByTopic(topicName)

        val quizQuestions = mutableListOf<QuizQuestion>()

        // 1. Add mistaken questions first as repeat candidates!
        mistakes.forEach { mockMistake ->
            quizQuestions.add(
                QuizQuestion(
                    id = mockMistake.questionId,
                    topicName = mockMistake.topicName,
                    questionText = mockMistake.questionText,
                    optionA = mockMistake.optionA,
                    optionB = mockMistake.optionB,
                    optionC = mockMistake.optionC,
                    optionD = mockMistake.optionD,
                    correctAnswer = mockMistake.correctAnswer,
                    explanation = mockMistake.explanation
                )
            )
        }

        // 2. Pad with remaining standard questions that are not already present
        val addedIds = quizQuestions.map { it.id }.toSet()
        val extraNeeded = quizSize - quizQuestions.size
        if (extraNeeded > 0) {
            val remainingCandidates = standardQuestions.filter { !addedIds.contains(it.id) }
            quizQuestions.addAll(remainingCandidates.shuffled().take(extraNeeded))
        }

        return quizQuestions.take(quizSize)
    }

    /**
     * Record mistaken answer.
     */
    suspend fun recordMistake(question: QuizQuestion) {
        dao.insertMistakenQuestion(
            MistakenQuestion(
                questionId = question.id,
                topicName = question.topicName,
                subjectName = MockStaticData.topics.find { it.name == question.topicName }?.subject ?: "GK GS",
                questionText = question.questionText,
                optionA = question.optionA,
                optionB = question.optionB,
                optionC = question.optionC,
                optionD = question.optionD,
                correctAnswer = question.correctAnswer,
                explanation = question.explanation
            )
        )
    }

    /**
     * Resolve mistaken answer (delete when finished correctly).
     */
    suspend fun resolveMistake(questionId: String) {
        dao.removeMistakenQuestionById(questionId)
    }

    // --- User Attempts & Dynamic Analytics ---
    val allAttempts: Flow<List<UserAttempt>> = dao.getAllAttempts()

    suspend fun saveQuizResult(
        category: String,
        topicName: String,
        subjectName: String,
        correctCount: Int,
        totalCount: Int
    ) {
        // Save score to history
        val attempt = UserAttempt(
            category = category,
            topicName = topicName,
            subjectName = subjectName,
            correctCount = correctCount,
            totalCount = totalCount
        )
        dao.insertAttempt(attempt)

        // Increment current user's score points in leaderboard!
        val currentLeaderboard = dao.getCurrentUserEntry()
        if (currentLeaderboard != null) {
            val pointsEarned = correctCount * 50 // 50 points per correct answer
            val updatedUser = currentLeaderboard.copy(
                scorePoints = currentLeaderboard.scorePoints + pointsEarned
            )
            dao.insertLeaderboardEntry(updatedUser)
            recalculateLeaderboardRanks()
        }
    }

    /**
     * Dynamically recalculates leaderboard positions after points are awarded.
     */
    private suspend fun recalculateLeaderboardRanks() {
        // Run synchronously with firstOrNull Flow fetch
        val entries = dao.getLeaderboard().firstOrNull() ?: return
        val sortedByScore = entries.sortedByDescending { it.scorePoints }
        val updatedRanks = sortedByScore.mapIndexed { index, entry ->
            entry.copy(rank = index + 1)
        }
        dao.insertLeaderboardEntries(updatedRanks)
    }

    // --- Student Leaderboard ---
    fun getLeaderboard(): Flow<List<LeaderboardEntry>> = dao.getLeaderboard()

    // --- Mistaken Questions State ---
    fun getAllMistakenQuestions(): Flow<List<MistakenQuestion>> = dao.getAllMistakenQuestions()

    // --- Notes Management ---
    val allNotes: Flow<List<UserNote>> = dao.getAllNotes()

    suspend fun saveNote(title: String, content: String, topicName: String) {
        val note = UserNote(title = title, content = content, topicName = topicName)
        dao.insertNote(note)
    }

    suspend fun deleteNote(id: Int) {
        dao.deleteNoteById(id)
    }
}
