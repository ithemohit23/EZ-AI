package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.QuizQuestion
import com.example.ui.ExamViewModel

@Composable
fun QuizScreen(
    viewModel: ExamViewModel,
    onNavigateBackToLearn: () -> Unit,
    modifier: Modifier = Modifier
) {
    val questions by viewModel.quizQuestions.collectAsStateWithLifecycle()
    val activeIdx by viewModel.currentQuestionIndex.collectAsStateWithLifecycle()
    val selectedOption by viewModel.selectedOption.collectAsStateWithLifecycle()
    val showExplanation by viewModel.showExplanation.collectAsStateWithLifecycle()
    val correctCount by viewModel.quizCorrectCount.collectAsStateWithLifecycle()
    val finished by viewModel.quizFinished.collectAsStateWithLifecycle()
    val isRescueMode by viewModel.quizIsReviewTheme.collectAsStateWithLifecycle()
    val isGeneratingQuiz by viewModel.isGeneratingQuiz.collectAsStateWithLifecycle()
    val activeExamLevel by viewModel.activeExamLevel.collectAsStateWithLifecycle()

    val scrollState = rememberScrollState()

    if (isGeneratingQuiz) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(64.dp),
                strokeWidth = 6.dp
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                "Forging custom level mock test...",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Gemini AI is analyzing $activeExamLevel syllabus metrics and custom crafting 3 exam-standard mock questions with descriptive explanations on the spot...",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        return
    }

    if (questions.isEmpty()) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.HourglassEmpty,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "No Active Practice Exam",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Please choose a syllabus topic in the Syllabus section to start your practice quiz, or trigger a complete Rescue Quiz of previous mistakes directly from the main Dashboard.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = onNavigateBackToLearn) {
                Text("Select Topic Syllabus")
            }
        }
        return
    }

    if (finished) {
        // --- QUIZ COMPLETED SUMMARY SCREEN ---
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(scrollState)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.WorkspacePremium,
                contentDescription = "Success Trophy",
                modifier = Modifier.size(80.dp),
                tint = Color(0xFFF9A825) // Golden
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Practice Complete!",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = if (isRescueMode) "Mistake Rescue Revision Completed" else "Syllabus Quiz Result ($activeExamLevel)",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Score circular badge
            Card(
                modifier = Modifier.size(140.dp),
                shape = CircleShape,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "${correctCount}/${questions.size}",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        "Solved",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            val pointsEarned = correctCount * 50
            Text(
                text = "+$pointsEarned Leaderboard XP Processed!",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2E7D32)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = if (isRescueMode) {
                    "Correct answers have been successfully cleared from your spaced mistake-repetition folder! Remaining items will stay in the vault."
                } else {
                    "Questions answered incorrectly have been filed automatically to your Spaced-Repetition folder on the Dashboard. Complete standard drills to practice them until mastered!"
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onNavigateBackToLearn,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Close and Study Other Subjects")
            }
        }
        return
    }

    // --- ACTIVE QUESTION DISPLAY ---
    val currentQuestion = questions.getOrNull(activeIdx) ?: return
    val totalQuestions = questions.size
    val progress = (activeIdx + 1).toFloat() / totalQuestions

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // Progress Meter header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if (isRescueMode) Icons.Default.Healing else Icons.Default.Timer,
                    contentDescription = null,
                    tint = if (isRescueMode) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (isRescueMode) "Mistake Revision Drill" else "Practice Exam",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (isRescueMode) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                )
            }
            Text(
                text = "Question ${activeIdx + 1} of $totalQuestions",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(CircleShape),
            color = if (isRescueMode) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Question card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "TOPIC: ${currentQuestion.topicName}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = currentQuestion.questionText,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 24.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Options A, B, C, D
        val options = listOf(
            "A" to currentQuestion.optionA,
            "B" to currentQuestion.optionB,
            "C" to currentQuestion.optionC,
            "D" to currentQuestion.optionD
        )

        options.forEach { (key, optText) ->
            val isSelected = selectedOption == key
            val isCorrectAnswer = currentQuestion.correctAnswer.equals(key, ignoreCase = true)
            val answered = selectedOption != null

            // Determine background button colors dynamically on submission and selection
            val containerColor = when {
                !answered -> MaterialTheme.colorScheme.surface
                isSelected && isCorrectAnswer -> Color(0xFFC8E6C9) // Green (Correct)
                isSelected && !isCorrectAnswer -> Color(0xFFFFCDD2) // Red (Wrong Selection)
                isCorrectAnswer -> Color(0xFFC8E6C9) // Highlight correct answers anyway
                else -> MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
            }

            val borderColor = when {
                !answered -> MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                isSelected && isCorrectAnswer -> Color(0xFF2E7D32)
                isSelected && !isCorrectAnswer -> Color(0xFFC62828)
                isCorrectAnswer -> Color(0xFF2E7D32)
                else -> Color.Transparent
            }

            Card(
                onClick = { viewModel.submitAnswer(key) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .testTag("option_$key"),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, borderColor),
                colors = CardDefaults.cardColors(containerColor = containerColor),
                enabled = !answered
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(
                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = key,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = optText,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )

                    // Display feedback icon
                    if (answered) {
                        if (isCorrectAnswer) {
                            Icon(Icons.Default.CheckCircle, contentDescription = "Correct", tint = Color(0xFF2E7D32))
                        } else if (isSelected) {
                            Icon(Icons.Default.Cancel, contentDescription = "Incorrect", tint = Color(0xFFC62828))
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // --- EXPLANATION PORTAL (SHOWN AFTER ANSWERING) ---
        AnimatedVisibility(
            visible = showExplanation,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.4f)),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary.copy(alpha = 0.25f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.AutoStories,
                            contentDescription = "Answer Key Analysis",
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Revision Explanation:",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = currentQuestion.explanation,
                        style = MaterialTheme.typography.bodyMedium,
                        lineHeight = 20.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Action Buttons at bottom
        if (selectedOption != null) {
            Button(
                onClick = { viewModel.nextQuestion() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("quiz_next_button"),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text(
                    text = if (activeIdx + 1 == totalQuestions) "Complete Practice Drill" else "Next Question",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.Default.ArrowForward, contentDescription = null)
            }
        }
    }
}
