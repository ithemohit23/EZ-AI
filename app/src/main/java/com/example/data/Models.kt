package com.example.data

import androidx.room.*

@Entity(tableName = "user_attempts")
data class UserAttempt(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val category: String, // SSC, Railway, CDS, NDA, Banking
    val topicName: String, // e.g. "Indian Constitution"
    val subjectName: String, // e.g. "Polity", "History", "Science"
    val correctCount: Int,
    val totalCount: Int,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "mistaken_questions")
data class MistakenQuestion(
    @PrimaryKey val questionId: String, // TopicName_index
    val topicName: String,
    val subjectName: String,
    val questionText: String,
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val optionD: String,
    val correctAnswer: String, // "A", "B", "C", "D"
    val explanation: String,
    val mistakeCount: Int = 1,
    val lastAttemptedTime: Long = System.currentTimeMillis()
)

@Entity(tableName = "user_notes")
data class UserNote(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val content: String,
    val topicName: String, // Associated topic
    val lastUpdated: Long = System.currentTimeMillis()
)

@Entity(tableName = "leaderboard")
data class LeaderboardEntry(
    @PrimaryKey val username: String,
    val scorePoints: Int,
    val rank: Int,
    val isCurrentUser: Boolean = false,
    val state: String = "Active"
)

// Data class for Subject & Topics (In-Memory/Local Static Resource)
data class GkGsTopic(
    val id: String,
    val name: String,
    val subject: String, // History, Polity, Geography, Science, Economy/Banking
    val videoId: String, // Simulated Video link or URL
    val videoTitle: String,
    val duration: String,
    val mindMapUrl: String, // custom mindmap key
    val revisionNotes: String,
    val mindMapNodes: List<MindMapNode> = emptyList()
)

data class MindMapNode(
    val id: String,
    val label: String,
    val x: Float, // Relative X coordinate (0.0 to 1.0)
    val y: Float, // Relative Y coordinate (0.0 to 1.0)
    val connections: List<String> = emptyList(), // connection to node ids
    val details: String = "" // Notes shown when clicked
)

data class QuizQuestion(
    val id: String,
    val topicName: String,
    val questionText: String,
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val optionD: String,
    val correctAnswer: String, // "A", "B", "C", "D"
    val explanation: String
)
