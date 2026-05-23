package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class StudentViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: StudentRepository

    // Database state flows
    val timetableEvents: StateFlow<List<TimetableEvent>>
    val attendanceList: StateFlow<List<SubjectAttendance>>
    val remindersList: StateFlow<List<Reminder>>
    val notesList: StateFlow<List<Note>>
    val habitsList: StateFlow<List<Habit>>
    val expensesList: StateFlow<List<Expense>>
    val coursesList: StateFlow<List<Course>>
    val chatMessages: StateFlow<List<ChatMessage>>

    // Pomodoro states
    private val _pomodoroTimeRemaining = MutableStateFlow(1500) // 25 minutes default
    val pomodoroTimeRemaining: StateFlow<Int> = _pomodoroTimeRemaining.asStateFlow()

    private val _pomodoroTotalDuration = MutableStateFlow(1500)
    val pomodoroTotalDuration: StateFlow<Int> = _pomodoroTotalDuration.asStateFlow()

    private val _isPomodoroRunning = MutableStateFlow(false)
    val isPomodoroRunning: StateFlow<Boolean> = _isPomodoroRunning.asStateFlow()

    private val _pomodoroMode = MutableStateFlow("Work") // "Work" or "Break"
    val pomodoroMode: StateFlow<String> = _pomodoroMode.asStateFlow()

    private var pomodoroJob: Job? = null

    // UI Dark/Light Theme override
    private val _isDarkTheme = MutableStateFlow(true) // Start with Notion dark mode as default
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()

    // Gemini API Study Assistant Chat state
    private val _isAiLoading = MutableStateFlow(false)
    val isAiLoading: StateFlow<Boolean> = _isAiLoading.asStateFlow()

    // Generated AI Plan State
    private val _aiGeneratedPlan = MutableStateFlow<String?>(null)
    val aiGeneratedPlan: StateFlow<String?> = _aiGeneratedPlan.asStateFlow()

    init {
        val database = AppDatabase.getDatabase(application)
        repository = StudentRepository(database)

        timetableEvents = repository.allEvents.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        attendanceList = repository.allAttendance.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        remindersList = repository.allReminders.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        notesList = repository.allNotes.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        habitsList = repository.allHabits.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        expensesList = repository.allExpenses.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        coursesList = repository.allCourses.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        chatMessages = repository.allMessages.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    }

    // Toggle Dark Theme
    fun toggleDarkTheme() {
        _isDarkTheme.value = !_isDarkTheme.value
    }

    // --- Timetable ---
    fun addTimetableEvent(dayOfWeek: String, subject: String, time: String, location: String, colorHex: String) {
        viewModelScope.launch {
            repository.insertEvent(
                TimetableEvent(
                    dayOfWeek = dayOfWeek,
                    subject = subject,
                    time = time,
                    location = location,
                    colorHex = colorHex
                )
            )
        }
    }

    fun deleteTimetableEvent(id: Int) {
        viewModelScope.launch {
            repository.deleteEventById(id)
        }
    }

    // --- Attendance Tracker ---
    fun addAttendanceSubject(name: String, attended: Int = 0, total: Int = 0) {
        viewModelScope.launch {
            repository.insertAttendance(
                SubjectAttendance(
                    subjectName = name,
                    attendedClasses = attended,
                    totalClasses = total
                )
            )
        }
    }

    fun adjustAttendance(id: Int, incrementAttended: Boolean, incrementTotal: Boolean) {
        viewModelScope.launch {
            val list = attendanceList.value
            val current = list.find { it.id == id } ?: return@launch
            var newAttended = current.attendedClasses
            var newTotal = current.totalClasses

            if (incrementTotal) {
                newTotal++
                if (incrementAttended) {
                    newAttended++
                }
            } else {
                // Decrement logic (optional safeguards)
                if (newTotal > 0) {
                    newTotal--
                    if (incrementAttended && newAttended > 0) {
                        newAttended--
                    }
                }
            }

            repository.updateAttendance(
                current.copy(
                    attendedClasses = newAttended,
                    totalClasses = newTotal
                )
            )
        }
    }

    fun resetAttendance(id: Int) {
        viewModelScope.launch {
            val list = attendanceList.value
            val current = list.find { it.id == id } ?: return@launch
            repository.updateAttendance(current.copy(attendedClasses = 0, totalClasses = 0))
        }
    }

    fun deleteAttendance(id: Int) {
        viewModelScope.launch {
            repository.deleteAttendanceById(id)
        }
    }

    // --- Reminders (Assignments & Exams) ---
    fun addReminder(title: String, description: String, dueDate: Long, type: String) {
        viewModelScope.launch {
            repository.insertReminder(
                Reminder(
                    title = title,
                    description = description,
                    dueDate = dueDate,
                    type = type
                )
            )
        }
    }

    fun toggleReminderCompleted(id: Int) {
        viewModelScope.launch {
            val list = remindersList.value
            val current = list.find { it.id == id } ?: return@launch
            repository.updateReminder(current.copy(isCompleted = !current.isCompleted))
        }
    }

    fun deleteReminder(id: Int) {
        viewModelScope.launch {
            repository.deleteReminderById(id)
        }
    }

    // --- Notes Storage with Image/PDF Support ---
    fun addNote(title: String, content: String, attachmentPath: String? = null, imagePath: String? = null) {
        viewModelScope.launch {
            repository.insertNote(
                Note(
                    title = title,
                    content = content,
                    attachmentPath = attachmentPath,
                    imagePath = imagePath,
                    timestamp = System.currentTimeMillis()
                )
            )
        }
    }

    fun deleteNote(id: Int) {
        viewModelScope.launch {
            repository.deleteNoteById(id)
        }
    }

    // --- Habits ---
    fun addHabit(name: String, frequency: String) {
        viewModelScope.launch {
            repository.insertHabit(Habit(name = name, frequency = frequency))
        }
    }

    fun checkInHabit(id: Int) {
        viewModelScope.launch {
            val list = habitsList.value
            val current = list.find { it.id == id } ?: return@launch
            val now = System.currentTimeMillis()

            // Reset streak to 1 if it has been completed, or increment streak
            val newStreak = current.streak + 1
            repository.updateHabit(
                current.copy(
                    streak = newStreak,
                    lastCompletedTimestamp = now
                )
            )
        }
    }

    fun deleteHabit(id: Int) {
        viewModelScope.launch {
            repository.deleteHabitById(id)
        }
    }

    // --- Expenses Tracker ---
    fun addExpense(description: String, amount: Double, category: String) {
        viewModelScope.launch {
            repository.insertExpense(
                Expense(
                    description = description,
                    amount = amount,
                    category = category,
                    timestamp = System.currentTimeMillis()
                )
            )
        }
    }

    fun deleteExpense(id: Int) {
        viewModelScope.launch {
            repository.deleteExpenseById(id)
        }
    }

    // --- CGPA Calculator (Courses) ---
    fun addCourse(name: String, semester: Int, grade: String, credits: Int) {
        viewModelScope.launch {
            repository.insertCourse(
                Course(
                    name = name,
                    semester = semester,
                    grade = grade,
                    credits = credits
                )
            )
        }
    }

    fun deleteCourse(id: Int) {
        viewModelScope.launch {
            repository.deleteCourseById(id)
        }
    }

    // Grade to GradePoints (4.0 Scale)
    fun getGradeValue(grade: String): Double {
        return when (grade.uppercase().trim()) {
            "A+", "A" -> 4.0
            "A-" -> 3.7
            "B+" -> 3.3
            "B" -> 3.0
            "B-" -> 2.7
            "C+" -> 2.3
            "C" -> 2.0
            "D" -> 1.0
            "F" -> 0.0
            else -> 4.0 // Assumption default
        }
    }

    fun calculateCgpa(): Double {
        val courses = coursesList.value
        if (courses.isEmpty()) return 0.0
        var totalPoints = 0.0
        var totalCredits = 0

        for (c in courses) {
            totalPoints += getGradeValue(c.grade) * c.credits
            totalCredits += c.credits
        }

        return if (totalCredits > 0) totalPoints / totalCredits else 0.0
    }

    // --- Pomodoro Focus Timer ---
    fun togglePomodoro() {
        if (_isPomodoroRunning.value) {
            pausePomodoro()
        } else {
            startPomodoro()
        }
    }

    private fun startPomodoro() {
        _isPomodoroRunning.value = true
        pomodoroJob = viewModelScope.launch {
            while (_isPomodoroRunning.value && _pomodoroTimeRemaining.value > 0) {
                delay(1000)
                _pomodoroTimeRemaining.value--
            }

            if (_pomodoroTimeRemaining.value == 0) {
                // Toggle mode automatically when finished
                playAlarm()
                if (_pomodoroMode.value == "Work") {
                    setPomodoroMode("Break", 300) // 5 minutes break
                } else {
                    setPomodoroMode("Work", 1500) // 25 minutes work
                }
                startPomodoro() // restart
            }
        }
    }

    fun pausePomodoro() {
        _isPomodoroRunning.value = false
        pomodoroJob?.cancel()
    }

    fun resetPomodoro() {
        pausePomodoro()
        val defaultSecs = if (_pomodoroMode.value == "Work") 1500 else 300
        _pomodoroTimeRemaining.value = defaultSecs
        _pomodoroTotalDuration.value = defaultSecs
    }

    fun setPomodoroMode(mode: String, durationSeconds: Int) {
        pausePomodoro()
        _pomodoroMode.value = mode
        _pomodoroTimeRemaining.value = durationSeconds
        _pomodoroTotalDuration.value = durationSeconds
    }

    private fun playAlarm() {
        // Log or trigger simulated notify callback for completeness
    }

    // --- AI Study chatbot ---
    fun sendChatMessage(text: String) {
        if (text.isBlank()) return
        viewModelScope.launch {
            // Save user message
            val userMsg = ChatMessage(text = text, sender = "user")
            repository.insertMessage(userMsg)

            _isAiLoading.value = true

            // Formulate query for the ChatBot
            val answer = GeminiClient.generateResponse(text)

            // Save AI message
            val aiMsg = ChatMessage(text = answer, sender = "assistant")
            repository.insertMessage(aiMsg)

            _isAiLoading.value = false
        }
    }

    fun clearChat() {
        viewModelScope.launch {
            repository.clearChatHistory()
        }
    }

    // --- AI study planner (Weekly planner auto-generator) ---
    fun generateAiStudyPlan() {
        viewModelScope.launch {
            _isAiLoading.value = true
            _aiGeneratedPlan.value = null

            // Construct contextual context based on existing courses & habits to make dynamic plans!
            val courses = coursesList.value
            val habits = habitsList.value

            val prompt = if (courses.isEmpty() && habits.isEmpty()) {
                "Generate a professional weekly college study schedule of time slots, including custom recommendations for focus, sleep, and assignment handling."
            } else {
                val coursesStr = courses.joinToString { "${it.name} (Grade Goal: ${it.grade})" }
                val habitsStr = habits.joinToString { it.name }
                "I am enrolled in these courses: [$coursesStr] and trying to maintain these habits: [$habitsStr]. Can you compile a highly personalized Weekly Study Plan for me with exact hour-by-hour focus slots, habit check-ins, and mock exam review strategies? Format it beautifully with bullet points."
            }

            val resultPlan = GeminiClient.generateResponse(prompt)
            _aiGeneratedPlan.value = resultPlan
            _isAiLoading.value = false

            // Auto-save this plan into our Notes Database so they can access it on any screen!
            repository.insertNote(
                Note(
                    title = "AI Weekly Study Planner Results",
                    content = resultPlan,
                    attachmentPath = "AI Sync",
                    timestamp = System.currentTimeMillis()
                )
            )
        }
    }
}

class StudentViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StudentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StudentViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
