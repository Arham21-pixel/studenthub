package com.example.data

import kotlinx.coroutines.flow.Flow

class StudentRepository(private val db: AppDatabase) {

    private val timetableDao = db.timetableDao()
    private val attendanceDao = db.attendanceDao()
    private val reminderDao = db.reminderDao()
    private val noteDao = db.noteDao()
    private val habitDao = db.habitDao()
    private val expenseDao = db.expenseDao()
    private val courseDao = db.courseDao()
    private val chatMessageDao = db.chatMessageDao()

    // Timetable
    val allEvents: Flow<List<TimetableEvent>> = timetableDao.getAllEvents()
    suspend fun insertEvent(event: TimetableEvent) = timetableDao.insertEvent(event)
    suspend fun deleteEventById(id: Int) = timetableDao.deleteEventById(id)

    // Attendance
    val allAttendance: Flow<List<SubjectAttendance>> = attendanceDao.getAllAttendance()
    suspend fun insertAttendance(attendance: SubjectAttendance) = attendanceDao.insertAttendance(attendance)
    suspend fun updateAttendance(attendance: SubjectAttendance) = attendanceDao.updateAttendance(attendance)
    suspend fun deleteAttendanceById(id: Int) = attendanceDao.deleteAttendanceById(id)

    // Reminders
    val allReminders: Flow<List<Reminder>> = reminderDao.getAllReminders()
    suspend fun insertReminder(reminder: Reminder) = reminderDao.insertReminder(reminder)
    suspend fun updateReminder(reminder: Reminder) = reminderDao.updateReminder(reminder)
    suspend fun deleteReminderById(id: Int) = reminderDao.deleteReminderById(id)

    // Notes
    val allNotes: Flow<List<Note>> = noteDao.getAllNotes()
    suspend fun insertNote(note: Note) = noteDao.insertNote(note)
    suspend fun deleteNoteById(id: Int) = noteDao.deleteNoteById(id)

    // Habits
    val allHabits: Flow<List<Habit>> = habitDao.getAllHabits()
    suspend fun insertHabit(habit: Habit) = habitDao.insertHabit(habit)
    suspend fun updateHabit(habit: Habit) = habitDao.updateHabit(habit)
    suspend fun deleteHabitById(id: Int) = habitDao.deleteHabitById(id)

    // Expenses
    val allExpenses: Flow<List<Expense>> = expenseDao.getAllExpenses()
    suspend fun insertExpense(expense: Expense) = expenseDao.insertExpense(expense)
    suspend fun deleteExpenseById(id: Int) = expenseDao.deleteExpenseById(id)

    // Courses (CGPA)
    val allCourses: Flow<List<Course>> = courseDao.getAllCourses()
    suspend fun insertCourse(course: Course) = courseDao.insertCourse(course)
    suspend fun deleteCourseById(id: Int) = courseDao.deleteCourseById(id)

    // Chat History
    val allMessages: Flow<List<ChatMessage>> = chatMessageDao.getAllMessages()
    suspend fun insertMessage(message: ChatMessage) = chatMessageDao.insertMessage(message)
    suspend fun clearChatHistory() = chatMessageDao.clearHistory()
}
