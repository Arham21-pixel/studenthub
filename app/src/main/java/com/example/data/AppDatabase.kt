package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        TimetableEvent::class,
        SubjectAttendance::class,
        Reminder::class,
        Note::class,
        Habit::class,
        Expense::class,
        Course::class,
        ChatMessage::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun timetableDao(): TimetableDao
    abstract fun attendanceDao(): AttendanceDao
    abstract fun reminderDao(): ReminderDao
    abstract fun noteDao(): NoteDao
    abstract fun habitDao(): HabitDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun courseDao(): CourseDao
    abstract fun chatMessageDao(): ChatMessageDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "student_life_hub_db"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
