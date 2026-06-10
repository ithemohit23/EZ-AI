package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ui.ExamViewModel
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme

import androidx.lifecycle.compose.collectAsStateWithLifecycle

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: ExamViewModel = viewModel()
            val themeMode by viewModel.themeMode.collectAsStateWithLifecycle()
            val isDark = when (themeMode) {
                "light" -> false
                "dark" -> true
                else -> androidx.compose.foundation.isSystemInDarkTheme()
            }
            MyApplicationTheme(darkTheme = isDark) {
                MainApp(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp(viewModel: ExamViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: "dashboard"

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        topBar = {
            if (currentRoute == "dashboard") {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // JD Avatar
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(MaterialTheme.colorScheme.primary, androidx.compose.foundation.shape.CircleShape),
                                contentAlignment = androidx.compose.ui.Alignment.Center
                            ) {
                                Text(
                                    "JD",
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                            }
                            Column {
                                Text(
                                    "Good Morning,",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    "Jaydeep Singh",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        IconButton(
                            onClick = { /* Action if needed */ },
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(14.dp))
                                .size(40.dp)
                        ) {
                            Icon(
                                Icons.Default.Notifications,
                                contentDescription = "Notifications",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Ask Gemini AI... search container
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                            .clickable { navController.navigate("ai") }
                            .padding(horizontal = 16.dp),
                        contentAlignment = androidx.compose.ui.Alignment.CenterStart
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .background(MaterialTheme.colorScheme.primary, androidx.compose.foundation.shape.CircleShape)
                                )
                                Text(
                                    text = "Ask Gemini AI...",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            // Icon with Gemini representation
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(
                                        brush = androidx.compose.ui.graphics.Brush.linearGradient(
                                            colors = listOf(
                                                MaterialTheme.colorScheme.primary,
                                                MaterialTheme.colorScheme.secondary
                                            )
                                        ),
                                        shape = RoundedCornerShape(10.dp)
                                    ),
                                contentAlignment = androidx.compose.ui.Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            } else {
                val title = when (currentRoute) {
                    "syllabus" -> "Subject Syllabus"
                    "lessons" -> "Interactive Lesson"
                    "quiz" -> "Practice Exam"
                    "ai" -> "Gemini AI Workspace"
                    "leaderboard" -> "Mock Championship"
                    else -> "EZ with AI"
                }

                TopAppBar(
                    title = {
                        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                            Icon(
                                imageVector = when (currentRoute) {
                                    "syllabus" -> Icons.Default.Book
                                    "lessons" -> Icons.Default.PlayCircle
                                    "quiz" -> Icons.Default.Quiz
                                    "ai" -> Icons.Default.AutoAwesome
                                    "leaderboard" -> Icons.Default.EmojiEvents
                                    else -> Icons.Default.School
                                },
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text(
                                text = title,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )
            }
        },
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .testTag("app_navigation_bar"),
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = currentRoute == "dashboard",
                    onClick = { navController.navigate("dashboard") { launchSingleTop = true } },
                    icon = { Icon(Icons.Default.Dashboard, contentDescription = "Dashboard") },
                    label = { Text("Home") },
                    modifier = Modifier.testTag("nav_dashboard")
                )
                NavigationBarItem(
                    selected = currentRoute == "syllabus",
                    onClick = { navController.navigate("syllabus") { launchSingleTop = true } },
                    icon = { Icon(Icons.Default.Book, contentDescription = "Syllabus") },
                    label = { Text("Syllabus") },
                    modifier = Modifier.testTag("nav_syllabus")
                )
                NavigationBarItem(
                    selected = currentRoute == "lessons",
                    onClick = { navController.navigate("lessons") { launchSingleTop = true } },
                    icon = { Icon(Icons.Default.PlayCircle, contentDescription = "Lessons") },
                    label = { Text("Learn") },
                    modifier = Modifier.testTag("nav_lessons")
                )
                NavigationBarItem(
                    selected = currentRoute == "quiz",
                    onClick = { navController.navigate("quiz") { launchSingleTop = true } },
                    icon = { Icon(Icons.Default.Quiz, contentDescription = "Quiz") },
                    label = { Text("Quiz") },
                    modifier = Modifier.testTag("nav_quiz")
                )
                NavigationBarItem(
                    selected = currentRoute == "ai",
                    onClick = { navController.navigate("ai") { launchSingleTop = true } },
                    icon = { Icon(Icons.Default.AutoAwesome, contentDescription = "AI Companion") },
                    label = { Text("AI") },
                    modifier = Modifier.testTag("nav_ai")
                )
                NavigationBarItem(
                    selected = currentRoute == "leaderboard",
                    onClick = { navController.navigate("leaderboard") { launchSingleTop = true } },
                    icon = { Icon(Icons.Default.EmojiEvents, contentDescription = "Leaderboard") },
                    label = { Text("Ranks") },
                    modifier = Modifier.testTag("nav_leaderboard")
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "dashboard",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("dashboard") {
                DashboardScreen(
                    viewModel = viewModel,
                    onNavigateToLearn = { navController.navigate("syllabus") },
                    onNavigateToQuiz = { navController.navigate("quiz") },
                    onNavigateToAi = { navController.navigate("ai") }
                )
            }
            composable("syllabus") {
                BrowseSubjectsScreen(
                    viewModel = viewModel,
                    onNavigateToLesson = { navController.navigate("lessons") }
                )
            }
            composable("lessons") {
                LessonsScreen(
                    viewModel = viewModel,
                    onNavigateToQuiz = { navController.navigate("quiz") }
                )
            }
            composable("quiz") {
                QuizScreen(
                    viewModel = viewModel,
                    onNavigateBackToLearn = { navController.navigate("syllabus") }
                )
            }
            composable("ai") {
                AiWorkspaceScreen(
                    viewModel = viewModel
                )
            }
            composable("leaderboard") {
                LeaderboardScreen(
                    viewModel = viewModel
                )
            }
        }
    }
}
