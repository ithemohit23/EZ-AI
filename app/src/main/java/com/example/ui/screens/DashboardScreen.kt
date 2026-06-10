package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.GkGsTopic
import com.example.data.MistakenQuestion
import com.example.ui.ExamViewModel
import com.example.ui.Proficiency

@Composable
fun DashboardScreen(
    viewModel: ExamViewModel,
    onNavigateToLearn: () -> Unit,
    onNavigateToQuiz: () -> Unit,
    onNavigateToAi: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val proficiencyMap by viewModel.subjectProficiency.collectAsStateWithLifecycle()
    val mistakes by viewModel.mistakenQuestions.collectAsStateWithLifecycle()
    val leaderboard by viewModel.leaderboard.collectAsStateWithLifecycle()
    val reminderEnabled by viewModel.reminderEnabled.collectAsStateWithLifecycle()
    val dailyGoalComplete by viewModel.dailyGoalComplete.collectAsStateWithLifecycle()

    val themeMode by viewModel.themeMode.collectAsStateWithLifecycle()
    val activeExamLevel by viewModel.activeExamLevel.collectAsStateWithLifecycle()
    val studyStreak by viewModel.studyStreak.collectAsStateWithLifecycle()

    val myLeaderboardEntry = leaderboard.find { it.isCurrentUser }
    val currentPoints = myLeaderboardEntry?.scorePoints ?: 0
    val currentRank = myLeaderboardEntry?.rank ?: 5

    var showReminderNotificationDialog by remember { mutableStateOf(false) }
    var lastNotificationTitle by remember { mutableStateOf("") }
    var lastNotificationBody by remember { mutableStateOf("") }

    if (showReminderNotificationDialog) {
        AlertDialog(
            onDismissRequest = { showReminderNotificationDialog = false },
            confirmButton = {
                TextButton(onClick = { showReminderNotificationDialog = false }) {
                    Text("Awesome")
                }
            },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.NotificationsActive, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Push Notification Sent!")
                }
            },
            text = {
                Column {
                    Text(
                        "We successfully triggered a direct Android notification to your system bar!",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Title: $lastNotificationTitle", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(lastNotificationBody, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("(You can swipe down from the top of your device/emulator to see the real icon and action item!)", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                }
            }
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp)
    ) {
        // --- 1. USER STAT OVERVIEW HERO CARD WITH EXAM SELECTION ---
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("dashboard_hero_card"),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Welcome, Scholar!",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.LocalFireDepartment,
                                    contentDescription = "Streak",
                                    tint = Color(0xFFFF6D00),
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "$studyStreak-Day Consecutive Streak!",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFFFF6D00)
                                )
                            }
                        }
                        IconButton(
                            onClick = {
                                viewModel.toggleReminder(!reminderEnabled)
                                val statusText = if (!reminderEnabled) "Active" else "Silenced"
                                lastNotificationTitle = "Daily Reminders $statusText"
                                lastNotificationBody = "Study reminder system status was toggled successfully."
                                com.example.ui.NotificationHelper.sendReminderNotification(
                                    context,
                                    lastNotificationTitle,
                                    lastNotificationBody
                                )
                                showReminderNotificationDialog = true
                            },
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f), CircleShape)
                        ) {
                            Icon(
                                imageVector = if (reminderEnabled) Icons.Default.NotificationsActive else Icons.Default.NotificationsOff,
                                contentDescription = "Daily study notification",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        StatItem(
                            icon = Icons.Default.WorkspacePremium,
                            value = "$currentPoints XP",
                            label = "Study Score"
                        )
                        Divider(modifier = Modifier.height(40.dp).width(1.dp).align(Alignment.CenterVertically))
                        StatItem(
                            icon = Icons.Default.Leaderboard,
                            value = "#$currentRank",
                            label = "Leaderboard Rank"
                        )
                        Divider(modifier = Modifier.height(40.dp).width(1.dp).align(Alignment.CenterVertically))
                        StatItem(
                            icon = Icons.Default.CheckCircle,
                            value = if (dailyGoalComplete) "100%" else "40%",
                            label = "Daily Goal",
                            tint = if (dailyGoalComplete) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Exam target selector chips
                    Divider(color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.1f))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Customize Focus: Target Mock Level",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("SSC Level", "CDS Level", "NDA Level").forEach { levelName ->
                            val isChosen = activeExamLevel == levelName
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        if (isChosen) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant.copy(
                                            alpha = 0.5f
                                        )
                                    )
                                    .border(
                                        width = 1.dp,
                                        color = if (isChosen) Color.Transparent else MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                            alpha = 0.15f
                                        ),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .clickable { viewModel.setExamLevel(levelName) }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = levelName,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isChosen) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }

        // --- 1B. LIGHT / DARK THEME SELECTOR & NOTIFICATION PUSH SIMULATOR ---
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Theme Option & Aesthetics",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(
                            Triple("light", "Light ☀️", "Force light minimalist colors"),
                            Triple("dark", "Dark 🌙", "Force dark energy-saving theme"),
                            Triple("system", "System ⚙️", "Adapt to operating system settings")
                        ).forEach { (modeCode, labelText, hintText) ->
                            val isSelected = themeMode == modeCode
                            Button(
                                onClick = { viewModel.setThemeMode(modeCode) },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                    contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                                shape = RoundedCornerShape(12.dp),
                                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = labelText,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.ExtraBold,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1.0f)) {
                            Text(
                                "Daily Study Reminder System",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "Forces a local notification to verify push schedules",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Button(
                            onClick = {
                                val levelTag = when (activeExamLevel) {
                                    "NDA Level" -> "⚔️ NDA General Studies Alert"
                                    "CDS Level" -> "🎓 CDS Officer Drill Alert"
                                    else -> "🎯 SSC Daily Prep Alert"
                                }
                                lastNotificationTitle = levelTag
                                lastNotificationBody = "Maintain your $studyStreak-day streak! Read Static GK classical dances or take a quick level-wise quiz."
                                com.example.ui.NotificationHelper.sendReminderNotification(
                                    context,
                                    lastNotificationTitle,
                                    lastNotificationBody
                                )
                                showReminderNotificationDialog = true
                            },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Simulate")
                        }
                    }
                }
            }
        }

        // --- 1C. SCHOLAR PROFILE & UNLOCKED BADGES ---
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "My Unlocked Gamification Badges",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Badge(containerColor = MaterialTheme.colorScheme.primaryContainer) {
                            Text("4 Badges", modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp), style = MaterialTheme.typography.labelSmall)
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        viewModel.unlockedBadges.forEach { badge ->
                            val bColor = when (badge.iconKey) {
                                "ic_award_star" -> Color(0xFF0288D1)
                                "ic_award_crown" -> Color(0xFFFBC02D)
                                "ic_award_fire" -> Color(0xFFF57C00)
                                "ic_award_shield" -> Color(0xFF388E3C)
                                else -> MaterialTheme.colorScheme.secondary
                            }
                            val bIcon = when (badge.iconKey) {
                                "ic_award_star" -> Icons.Default.Star
                                "ic_award_crown" -> Icons.Default.EmojiEvents
                                "ic_award_fire" -> Icons.Default.LocalFireDepartment
                                "ic_award_shield" -> Icons.Default.Shield
                                else -> Icons.Default.Star
                            }

                            Card(
                                modifier = Modifier
                                    .width(140.dp)
                                    .padding(vertical = 4.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(45.dp)
                                            .background(bColor.copy(alpha = 0.15f), CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = bIcon,
                                            contentDescription = badge.name,
                                            tint = bColor,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = badge.name,
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = badge.description,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontSize = 9.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        textAlign = TextAlign.Center,
                                        lineHeight = 11.sp,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // --- 2. PERSONALIZED DASHBOARD: ANALYZE STRENGTHS & WEAKNESSES ---
        item {
            Text(
                text = "Subject Highlights & Proficiency",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    proficiencyMap.values.forEach { pref ->
                        ProficiencyRow(pref)
                    }
                }
            }
        }

        // --- 3. SPACED MISTAKE-REPETITION (RESCUE PORTAL) ---
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Smart Revision Mode",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Badge(
                    containerColor = if (mistakes.isNotEmpty()) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                ) {
                    Text(
                        "${mistakes.size} mistakes active",
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }

        if (mistakes.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.Celebration,
                            contentDescription = "Zero Mistakes",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Flawless History!",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Complete quizzes in other topics. If you make mistakes, they will lock here for repetitive training.",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 8.dp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(onClick = onNavigateToLearn) {
                            Text("Browse GK GS Syllabus")
                        }
                    }
                }
            }
        } else {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.15f)),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.25f))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            "Mistake Repetition Portal",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.error
                        )
                        Text(
                            "These are quiz questions you answered incorrectly. You must solve them correctly in a mock review to erase them entirely.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                viewModel.startRescueQuiz()
                                onNavigateToQuiz()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("start_rescue_quiz_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                        ) {
                            Icon(Icons.Default.Healing, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Start Rescue Quiz (${mistakes.size})")
                        }
                    }
                }
            }

            items(mistakes.take(3)) { mistake ->
                MistakeShortItem(mistake)
            }
        }

        // --- 4. QUICK PORTALS ---
        item {
            Text(
                text = "Jump Into Action",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ActionButtonCard(
                    title = "Consult Gemini AI",
                    subtitle = "Instant GK Explanations",
                    icon = Icons.Default.AutoAwesome,
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    onNavigate = onNavigateToAi,
                    modifier = Modifier.weight(1f)
                )
                ActionButtonCard(
                    title = "Interactive Notes",
                    subtitle = "Notes & Mind Maps",
                    icon = Icons.Default.BubbleChart,
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    onNavigate = onNavigateToLearn,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun StatItem(
    icon: ImageVector,
    value: String,
    label: String,
    tint: Color = MaterialTheme.colorScheme.primary
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(28.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
fun ProficiencyRow(pref: Proficiency) {
    val progressAnim by animateFloatAsState(
        targetValue = pref.percentage,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "profProgress"
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = pref.subjectName,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "${pref.correct}/${pref.total} solved (${(pref.percentage * 100).toInt()}%)",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Box(modifier = Modifier.fillMaxWidth()) {
            LinearProgressIndicator(
                progress = { progressAnim },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(CircleShape),
                color = when {
                    pref.percentage >= 0.8f -> Color(0xFF2E7D32) // Healthy Green
                    pref.percentage >= 0.5f -> Color(0xFFF9A825) // Medium Yellow
                    pref.percentage > 0f -> Color(0xFFC62828) // Weak Red
                    else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                }
            )
        }
    }
}

@Composable
fun MistakeShortItem(mistake: MistakenQuestion) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.2f)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(MaterialTheme.colorScheme.error.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.PriorityHigh,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = mistake.topicName,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = mistake.questionText,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline
            )
        }
    }
}

@Composable
fun ActionButtonCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    containerColor: Color,
    onNavigate: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Generate a tinted color based on current layout index/role
    val iconTintBg = containerColor.copy(alpha = 0.12f)
    val iconTintMain = containerColor

    Card(
        onClick = onNavigate,
        modifier = modifier.height(130.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .background(iconTintBg, RoundedCornerShape(12.dp))
                    .padding(8.dp)
            ) {
                Icon(icon, contentDescription = null, tint = iconTintMain, modifier = Modifier.size(18.dp))
            }
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
