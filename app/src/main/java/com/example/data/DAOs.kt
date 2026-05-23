package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TimetableDao {
    @Query("SELECT * FROM timetable_events ORDER BY dayOfWeek ASC, time ASC")
    fun getAllEvents(): Flow<List<TimetableEvent>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: TimetableEvent)

    @Query("DELETE FROM timetable_events WHERE id = :id")
    suspend fun deleteEventById(id: Int)
}

@Dao
interface AttendanceDao {
    @Query("SELECT * FROM subject_attendance ORDER BY subjectName ASC")
    fun getAllAttendance(): Flow<List<SubjectAttendance>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttendance(attendance: SubjectAttendance)

    @Update
    suspend fun updateAttendance(attendance: SubjectAttendance)

    @Query("DELETE FROM subject_attendance WHERE id = :id")
    suspend fun deleteAttendanceById(id: Int)
}

@Dao
interface ReminderDao {
    @Query("SELECT * FROM reminders ORDER BY dueDate ASC")
    fun getAllReminders(): Flow<List<Reminder>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminder(reminder: Reminder)

    @Update
    suspend fun updateReminder(reminder: Reminder)

    @Query("DELETE FROM reminders WHERE id = :id")
    suspend fun deleteReminderById(id: Int)
}

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes ORDER BY timestamp DESC")
    fun getAllNotes(): Flow<List<Note>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Query("DELETE FROM notes WHERE id = :id")
    suspend fun deleteNoteById(id: Int)
}

@Dao
interface HabitDao {
    @Query("SELECT * FROM habits ORDER BY name ASC")
    fun getAllHabits(): Flow<List<Habit>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: Habit)

    @Update
    suspend fun updateHabit(habit: Habit)

    @Query("DELETE FROM habits WHERE id = :id")
    suspend fun deleteHabitById(id: Int)
}

@Dao
interface ExpenseDao {
    @Query("SELECT * FROM expenses ORDER BY timestamp DESC")
    fun getAllExpenses(): Flow<List<Expense>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: Expense)

    @Query("DELETE FROM expenses WHERE id = :id")
    suspend fun deleteExpenseById(id: Int)
}

@Dao
interface CourseDao {
    @Query("SELECT * FROM courses ORDER BY semester ASC, name ASC")
    fun getAllCourses(): Flow<List<Course>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourse(course: Course)

    @Query("DELETE FROM courses WHERE id = :id")
    suspend fun deleteCourseById(id: Int)
}

@Dao
interface ChatMessageDao {
    @Query("SELECT * FROM chat_messages ORDER BY timestamp ASC")
    fun getAllMessages(): Flow<List<ChatMessage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatMessage)

    @Query("DELETE FROM chat_messages")
    suspend fun clearHistory()
}
