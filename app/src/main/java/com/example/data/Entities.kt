package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "timetable_events")
data class TimetableEvent(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val dayOfWeek: String, // "Monday", "Tuesday", etc.
    val subject: String,
    val time: String,      // e.g., "10:00 AM - 11:30 AM"
    val location: String,  // e.g., "Room 401"
    val colorHex: String   // e.g., "#6366F1"
) : Serializable

@Entity(tableName = "subject_attendance")
data class SubjectAttendance(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val subjectName: String,
    val attendedClasses: Int = 0,
    val totalClasses: Int = 0
) : Serializable

@Entity(tableName = "reminders")
data class Reminder(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val dueDate: Long,     // Timestamp in ms
    val type: String,      // "Assignment" or "Exam"
    val isCompleted: Boolean = false
) : Serializable

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val content: String,
    val attachmentPath: String? = null, // Mocked PDF path or text attachment
    val imagePath: String? = null,      // Optional local Uri or path for visual proof
    val timestamp: Long = System.currentTimeMillis()
) : Serializable

@Entity(tableName = "habits")
data class Habit(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val frequency: String, // "Daily" or "Weekly"
    val streak: Int = 0,
    val lastCompletedTimestamp: Long = 0L
) : Serializable

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val description: String,
    val amount: Double,
    val category: String,  // "Food", "Hostel", "Books", "Leisure", "Misc"
    val timestamp: Long = System.currentTimeMillis()
) : Serializable

@Entity(tableName = "courses")
data class Course(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val semester: Int,     // Sem 1, Sem 2, etc.
    val name: String,
    val grade: String,     // "A+", "A", "B", "C", etc.
    val credits: Int
) : Serializable

@Entity(tableName = "chat_messages")
data class ChatMessage(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val text: String,
    val sender: String,    // "user" or "assistant"
    val timestamp: Long = System.currentTimeMillis()
) : Serializable
