package com.example.data

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.flow.Flow

@Dao
interface ExamBookDao {
    // User Quiz Attempts / Strengths & Weaknesses
    @Query("SELECT * FROM user_attempts ORDER BY timestamp DESC")
    fun getAllAttempts(): Flow<List<UserAttempt>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttempt(attempt: UserAttempt)

    @Query("SELECT * FROM user_attempts WHERE subjectName = :subjectName")
    suspend fun getAttemptsBySubject(subjectName: String): List<UserAttempt>

    // Mistaken Questions - For spacing/mistake repetition
    @Query("SELECT * FROM mistaken_questions WHERE topicName = :topicName")
    suspend fun getMistakenQuestionsForTopic(topicName: String): List<MistakenQuestion>

    @Query("SELECT * FROM mistaken_questions")
    fun getAllMistakenQuestions(): Flow<List<MistakenQuestion>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMistakenQuestion(question: MistakenQuestion)

    @Delete
    suspend fun removeMistakenQuestion(question: MistakenQuestion)

    @Query("DELETE FROM mistaken_questions WHERE questionId = :questionId")
    suspend fun removeMistakenQuestionById(questionId: String)

    // User Notes
    @Query("SELECT * FROM user_notes ORDER BY lastUpdated DESC")
    fun getAllNotes(): Flow<List<UserNote>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: UserNote): Long

    @Query("DELETE FROM user_notes WHERE id = :id")
    suspend fun deleteNoteById(id: Int)

    // Leaderboard
    @Query("SELECT * FROM leaderboard ORDER BY scorePoints DESC")
    fun getLeaderboard(): Flow<List<LeaderboardEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLeaderboardEntry(entry: LeaderboardEntry)

    @Query("SELECT * FROM leaderboard WHERE isCurrentUser = 1 LIMIT 1")
    suspend fun getCurrentUserEntry(): LeaderboardEntry?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLeaderboardEntries(entries: List<LeaderboardEntry>)
}

@Database(
    entities = [
        UserAttempt::class,
        MistakenQuestion::class,
        UserNote::class,
        LeaderboardEntry::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract val dao: ExamBookDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "exambook_db"
                )
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // Inject initial mock scores for competition on a background thread
                        db.execSQL("INSERT OR REPLACE INTO leaderboard (username, scorePoints, rank, isCurrentUser, state) VALUES ('Rohan Sharma', 1560, 1, 0, 'Active')")
                        db.execSQL("INSERT OR REPLACE INTO leaderboard (username, scorePoints, rank, isCurrentUser, state) VALUES ('Anjali Patel', 1340, 2, 0, 'Active')")
                        db.execSQL("INSERT OR REPLACE INTO leaderboard (username, scorePoints, rank, isCurrentUser, state) VALUES ('Vikram Singh', 1120, 3, 0, 'Active')")
                        db.execSQL("INSERT OR REPLACE INTO leaderboard (username, scorePoints, rank, isCurrentUser, state) VALUES ('Meera Nair', 950, 4, 0, 'Active')")
                        db.execSQL("INSERT OR REPLACE INTO leaderboard (username, scorePoints, rank, isCurrentUser, state) VALUES ('You (Aspirant)', 0, 5, 1, 'Active')")
                    }
                })
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
