package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.GkGsTopic
import com.example.data.MindMapNode
import com.example.ui.ExamViewModel

@Composable
fun LessonsScreen(
    viewModel: ExamViewModel,
    onNavigateToQuiz: () -> Unit,
    modifier: Modifier = Modifier
) {
    val topic by viewModel.activeTopic.collectAsStateWithLifecycle()
    val isPlaying by viewModel.isVideoPlaying.collectAsStateWithLifecycle()
    val playbackProgress by viewModel.videoPlaybackProgress.collectAsStateWithLifecycle()

    if (topic == null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Select a topic from the Syllabus first.")
        }
        return
    }

    val currentTopic = topic!!
    val scrollState = rememberScrollState()

    // Interactive Node State for Mind Map Canvas
    var selectedNode by remember { mutableStateOf<MindMapNode?>(currentTopic.mindMapNodes.firstOrNull()) }

    LaunchedEffect(currentTopic) {
        // Automatically default selected node on topic change
        selectedNode = currentTopic.mindMapNodes.firstOrNull()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
    ) {
        // --- 1. SIMULATED INTERACTIVE VIDEO PLAYER ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .background(Color.Black)
                .testTag("interactive_video_player")
        ) {
            // Simulated video poster or animated particle gradient
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                            colors = listOf(Color(0xFF1A1A1A), Color(0xFF0D0D0D))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.OndemandVideo,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = currentTopic.videoTitle,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                    Text(
                        text = if (isPlaying) "Playing Interactive Lecture..." else "Video Paused",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isPlaying) MaterialTheme.colorScheme.primary else Color.LightGray,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            // Playback controls overlaid at bottom
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(Color.Black.copy(alpha = 0.6f))
                    .padding(8.dp)
            ) {
                // Progress Slider
                Slider(
                    value = playbackProgress,
                    onValueChange = { viewModel.updateVideoPlayback(it) },
                    modifier = Modifier.height(20.dp),
                    colors = SliderDefaults.colors(
                        activeTrackColor = MaterialTheme.colorScheme.primary,
                        inactiveTrackColor = Color.DarkGray
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { viewModel.toggleVideoPlay() }) {
                            Icon(
                                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = "Play/Pause video",
                                tint = Color.White
                            )
                        }

                        Text(
                            text = "SSC & CDS Syllabus Track • GK GS",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Gray
                        )
                    }

                    Box(
                        modifier = Modifier
                            .background(Color.DarkGray, RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "HD 1080p",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.LightGray
                        )
                    }
                }
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {

            // --- 2. HEADER DETAILS ---
            Text(
                text = currentTopic.name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Syllabus Group: ssc, railway, bnk, defense",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "• Study Duration: ${currentTopic.duration}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.outline
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- 3. INTERACTIVE MIND MAP CANVAS CARD ---
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Interactive Mind Map (Tap Nodes to Reveal Detail)",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    // THE MIND MAP CANVAS
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp)
                            .background(
                                MaterialTheme.colorScheme.background,
                                RoundedCornerShape(16.dp)
                            )
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                                RoundedCornerShape(16.dp)
                            )
                    ) {
                        val nodes = currentTopic.mindMapNodes
                        val themePrimary = MaterialTheme.colorScheme.primary
                        val themeSecondary = MaterialTheme.colorScheme.secondary

                        // Render nodes and connect lines using Canvas
                        Canvas(
                            modifier = Modifier
                                .fillMaxSize()
                                .pointerInput(currentTopic) {
                                    detectTapGestures { offset ->
                                        // Dynamic check for which node has been tapped
                                        val canvasWidth = size.width
                                        val canvasHeight = size.height

                                        val tappedNode = nodes.find { node ->
                                            val nodeX = node.x * canvasWidth
                                            val nodeY = node.y * canvasHeight
                                            val distance = Math.hypot(
                                                (offset.x - nodeX).toDouble(),
                                                (offset.y - nodeY).toDouble()
                                            )
                                            distance <= 45 // node radial boundary hit check
                                        }

                                        if (tappedNode != null) {
                                            selectedNode = tappedNode
                                        }
                                    }
                                }
                        ) {
                            val w = size.width
                            val h = size.height

                            // 1. Draw connections first (layer behind nodes)
                            nodes.forEach { node ->
                                val startX = node.x * w
                                val startY = node.y * h
                                node.connections.forEach { targetId ->
                                    val target = nodes.find { it.id == targetId }
                                    if (target != null) {
                                        val endX = target.x * w
                                        val endY = target.y * h
                                        drawLine(
                                            color = themePrimary.copy(alpha = 0.35f),
                                            start = Offset(startX, startY),
                                            end = Offset(endX, endY),
                                            strokeWidth = 4f
                                        )
                                    }
                                }
                            }

                            // 2. Draw node points
                            nodes.forEach { node ->
                                val nodeX = node.x * w
                                val nodeY = node.y * h
                                val isSelected = selectedNode?.id == node.id

                                // Outer ring decoration
                                drawCircle(
                                    color = if (isSelected) themePrimary else themePrimary.copy(alpha = 0.2f),
                                    radius = if (isSelected) 24f else 18f,
                                    center = Offset(nodeX, nodeY),
                                    style = Stroke(width = 4f)
                                )

                                // Solid core circle
                                drawCircle(
                                    color = if (isSelected) themeSecondary else themePrimary,
                                    radius = if (isSelected) 14f else 10f,
                                    center = Offset(nodeX, nodeY)
                                )
                            }
                        }

                        // Labels floating overlay
                        nodes.forEach { node ->
                            val isSelected = selectedNode?.id == node.id
                            Box(
                                modifier = Modifier
                                    .offset(
                                        x = (node.x * 290).dp, // safe scaling for simple coordinate offset layout mapping
                                        y = (node.y * 190 + 12).dp // shift below center point
                                    )
                                    .background(
                                        if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                                        RoundedCornerShape(4.dp)
                                    )
                                    .padding(horizontal = 4.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = node.label,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 8.sp,
                                    maxLines = 1
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // NODE DETAILS REVEAL CARD
                    AnimatedVisibility(
                        visible = selectedNode != null,
                        enter = expandVertically() + fadeIn(),
                        exit = shrinkVertically() + fadeOut()
                    ) {
                        selectedNode?.let { node ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f))
                            ) {
                                Row(modifier = Modifier.padding(12.dp)) {
                                    Icon(
                                        Icons.Default.Info,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = node.label,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        Text(
                                            text = node.details,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- 3B. AIStoryteller cinematic video script generator ---
            val isGeneratingStory by viewModel.isGeneratingStory.collectAsStateWithLifecycle()
            val aiLectureStory by viewModel.aiLectureStory.collectAsStateWithLifecycle()

            var storyLanguage by remember { mutableStateOf("Hinglish") }
            var storyStyle by remember { mutableStateOf("Cinematic Action Storyteller with Fights & Visuals") }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.25f)
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary.copy(alpha = 0.4f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.tertiary,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Gemini AI Cinematic Video Storyteller",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                        }
                        Box(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.tertiary, RoundedCornerShape(8.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                "AI-Powered",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onTertiary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Don't just memorize — visualize! Let Gemini AI generate an eye-popping visual video storyboard for \"${currentTopic.name}\" styled with fights, scenes, and epic battle visuals described step-by-step.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.82f)
                    )

                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        "1. Select Video Delivery Style",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(
                            "Cinematic Action Storyteller with Fights & Visuals",
                            "Simple Topic Simplifier"
                        ).forEach { styleOption ->
                            val isChosen = storyStyle == styleOption
                            Button(
                                onClick = { storyStyle = styleOption },
                                modifier = Modifier.weight(1f),
                                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 6.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isChosen) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                                    contentColor = if (isChosen) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = if (styleOption.contains("Cinematic")) "⚔️ Battle Cinematic" else "💡 Simply Explain",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "2. Select Oral Script Language",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("Hinglish", "English", "Hindi").forEach { langOption ->
                            val isChosen = storyLanguage == langOption
                            Button(
                                onClick = { storyLanguage = langOption },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isChosen) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                                    contentColor = if (isChosen) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = langOption,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (!isGeneratingStory && aiLectureStory == null) {
                        Button(
                            onClick = {
                                viewModel.generateStoryLecture(currentTopic.name, storyStyle, storyLanguage)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Icon(Icons.Default.RocketLaunch, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Generate AI Cinematic Lecture Plan", fontWeight = FontWeight.Bold)
                        }
                    }

                    if (isGeneratingStory) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.tertiary)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                "Choreographing battlefields & translating into $storyLanguage script. Please stand by for Gemini's storyboard...",
                                style = MaterialTheme.typography.bodySmall,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                        }
                    }

                    aiLectureStory?.let { scriptText ->
                        Spacer(modifier = Modifier.height(12.dp))
                        Divider(color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f))
                        Spacer(modifier = Modifier.height(12.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.85f)),
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary.copy(alpha = 0.6f))
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        "🎬 CINEMATIC STORYBOARD SCRIPT",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFFFD600)
                                    )
                                    IconButton(
                                        onClick = { viewModel.resetStory() },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.Close,
                                            contentDescription = "Reset Story script",
                                            tint = Color.White,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(
                                    text = scriptText,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.LightGray,
                                    lineHeight = 22.sp,
                                    fontWeight = FontWeight.Normal
                                )

                                Spacer(modifier = Modifier.height(12.dp))
                                Button(
                                    onClick = {
                                        viewModel.toggleVideoPlay()
                                        // Auto simulation feedback
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD600)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("🎥 Simulate Playback of this Storyboard", color = Color.Black, fontWeight = FontWeight.Black)
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text(
                "Syllabus Revision Notes & Bulletins",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = currentTopic.revisionNotes,
                        style = MaterialTheme.typography.bodyMedium,
                        lineHeight = 22.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- 5. START TOPIC QUIZ CTA ---
            val activeExamLevel by viewModel.activeExamLevel.collectAsStateWithLifecycle()
            val isGeneratingQuiz by viewModel.isGeneratingQuiz.collectAsStateWithLifecycle()

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
            ) {
                Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Subject & Topic Mock Quiz Panel",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Target Focus: $activeExamLevel (Adjust on main Dashboard)",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    if (isGeneratingQuiz) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Gemini AI is parsing $activeExamLevel syllabus metrics and custom crafting 3 exam-standard mock questions...",
                                style = MaterialTheme.typography.bodySmall,
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Button(
                                onClick = {
                                    viewModel.startTopicQuiz(currentTopic)
                                    onNavigateToQuiz()
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                            ) {
                                Icon(Icons.Default.Quiz, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Standard Quiz", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }

                            Button(
                                onClick = {
                                    viewModel.generateAiLevelQuiz(currentTopic, activeExamLevel)
                                    onNavigateToQuiz()
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                            ) {
                                Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Generate $activeExamLevel", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}
