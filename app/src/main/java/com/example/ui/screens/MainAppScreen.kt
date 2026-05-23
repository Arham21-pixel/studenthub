package com.example.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.*
import com.example.ui.StudentViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainAppScreen(
    viewModel: StudentViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isDarkTheme by viewModel.isDarkTheme.collectAsStateWithLifecycle()

    // Internal Onboarding state
    var isUserOnboarded by remember { mutableStateOf(false) }

    // Notion Accent Palette definitions
    val accentViolet = Color(0xFF8B5CF6)
    val accentEmerald = Color(0xFF10B981)
    val accentOrange = Color(0xFFF59E0B)
    val accentBlue = Color(0xFF3B82F6)
    val accentPink = Color(0xFFEC4899)

    val customColors = if (isDarkTheme) {
        darkColorScheme(
            primary = accentViolet,
            secondary = accentEmerald,
            tertiary = accentOrange,
            background = Color(0xFF0C0E17), // Deep space indigo background
            surface = Color(0x99181B26),    // Glassy semi-transparent surface (60% alpha)
            onBackground = Color(0xFFF3F4F6),
            onSurface = Color(0xFFE5E7EB),
            onPrimary = Color.White
        )
    } else {
        lightColorScheme(
            primary = Color(0xFF4F46E5),    // Modern premium indigo
            secondary = Color(0xFF0D9488),
            tertiary = Color(0xFFEA580C),
            background = Color(0xFFEEF2FF), // Warm Indigo-tinged light backdrop
            surface = Color(0x99FFFFFF),    // Glassy semi-transparent white! (60% alpha)
            onBackground = Color(0xFF1E293B),
            onSurface = Color(0xFF0F172A),
            onPrimary = Color.White
        )
    }

    MaterialTheme(
        colorScheme = customColors
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .glowingGlassBackground(isDarkTheme)
        ) {
            if (!isUserOnboarded) {
                OnboardingLayout(
                    isDarkTheme = isDarkTheme,
                    onGetStarted = { isUserOnboarded = true },
                    onToggleTheme = { viewModel.toggleDarkTheme() }
                )
            } else {
                MainAppLayout(
                    viewModel = viewModel,
                    isDarkTheme = isDarkTheme
                )
            }
        }
    }
}

// ======================== ONBOARDING COMPONENT ========================
@Composable
fun OnboardingLayout(
    isDarkTheme: Boolean,
    onGetStarted: () -> Unit,
    onToggleTheme: () -> Unit
) {
    var step by remember { mutableStateOf(0) }
    val gradientBrushes = Brush.linearGradient(
        colors = listOf(Color(0xFF8B5CF6), Color(0xFF3B82F6))
    )

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "STUDENT LIFE HUB",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    letterSpacing = 2.sp
                )
                IconButton(onClick = onToggleTheme) {
                    Icon(
                        imageVector = if (isDarkTheme) Icons.Outlined.LightMode else Icons.Outlined.DarkMode,
                        contentDescription = "Theme Toggle",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        },
        containerColor = Color.Transparent
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Animated Slide Content
            AnimatedContent(
                targetState = step,
                transitionSpec = {
                    (slideInHorizontally { width -> width } + fadeIn() togetherWith
                            slideOutHorizontally { width -> -width } + fadeOut())
                        .using(SizeTransform(clip = false))
                }, label = "OnboardingContent"
            ) { targetStep ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(240.dp)
                            .clip(RoundedCornerShape(32.dp))
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        Color(0xFF8B5CF6).copy(alpha = 0.3f),
                                        Color.Transparent
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        when (targetStep) {
                            0 -> Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = null,
                                modifier = Modifier.size(110.dp).testTag("icon_step_0"),
                                tint = Color(0xFF8B5CF6)
                            )
                            1 -> Icon(
                                imageVector = Icons.Default.MenuBook,
                                contentDescription = null,
                                modifier = Modifier.size(110.dp).testTag("icon_step_1"),
                                tint = Color(0xFF10B981)
                            )
                            2 -> Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                modifier = Modifier.size(110.dp).testTag("icon_step_2"),
                                tint = Color(0xFFF59E0B)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    val (title, description) = when (targetStep) {
                        0 -> "Plan Your Semester" to "Manage weekly timetables easily and monitor your attendance percentages dynamically to ensure you stay above college limits."
                        1 -> "Store Notes & Track Goals" to "Safekeeping of classroom lectures with file attachments indicators. Establish routines, track financial logs, and see daily habit completion streaks."
                        2 -> "AI Study Companion & Timer" to "Call Gemini AI to generate custom hour-by-hour study planners automatically, chat to resolve complex curriculum, and clock focused states with the Pomodoro widget."
                        else -> "" to ""
                    }

                    Text(
                        text = title,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = description,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                }
            }

            // Stepper buttons control
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Indicator dots
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(3) { index ->
                        val isSelected = step == index
                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .height(8.dp)
                                .width(if (isSelected) 24.dp else 8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(
                                    if (isSelected) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)
                                )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                Button(
                    onClick = {
                        if (step < 2) {
                            step++
                        } else {
                            onGetStarted()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("onboarding_next_button"),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = if (step == 2) "Get Started" else "Continue",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}

// ======================== MAIN HOUSING LAYOUT ========================
@Composable
fun MainAppLayout(
    viewModel: StudentViewModel,
    isDarkTheme: Boolean
) {
    var activeTab by remember { mutableStateOf(0) }

    // Floating dialog flags
    var showEventDialog by remember { mutableStateOf(false) }
    var showSubjectDialog by remember { mutableStateOf(false) }
    var showReminderDialog by remember { mutableStateOf(false) }
    var showNoteDialog by remember { mutableStateOf(false) }
    var showHabitDialog by remember { mutableStateOf(false) }
    var showExpenseDialog by remember { mutableStateOf(false) }
    var showCourseDialog by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .navigationBarsPadding(),
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                val tabs = listOf(
                    Triple("Hub", Icons.Default.Dashboard, Icons.Outlined.Dashboard),
                    Triple("Calendar", Icons.Default.CalendarMonth, Icons.Outlined.CalendarMonth),
                    Triple("Notes", Icons.Default.Description, Icons.Outlined.Description),
                    Triple("AI Chat", Icons.Default.AutoAwesome, Icons.Outlined.AutoAwesome),
                    Triple("Finance", Icons.Default.School, Icons.Outlined.School)
                )

                tabs.forEachIndexed { idx, tab ->
                    NavigationBarItem(
                        selected = activeTab == idx,
                        onClick = { activeTab = idx },
                        icon = {
                            Icon(
                                imageVector = if (activeTab == idx) tab.second else tab.third,
                                contentDescription = tab.first
                            )
                        },
                        label = { Text(tab.first, fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                            unselectedTextColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                            indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                        ),
                        modifier = Modifier.testTag("nav_item_$idx")
                    )
                }
            }
        },
        floatingActionButton = {
            if (activeTab != 3) { // Hide FAB in AI Chat
                ExtendedFloatingActionButton(
                    onClick = {
                        when (activeTab) {
                            0 -> showHabitDialog = true
                            1 -> showEventDialog = true
                            2 -> showNoteDialog = true
                            4 -> showCourseDialog = true
                        }
                    },
                    icon = { Icon(Icons.Default.Add, contentDescription = "Add Item") },
                    text = {
                        Text(
                            when (activeTab) {
                                0 -> "New Goal"
                                1 -> "New Event"
                                2 -> "New Note"
                                else -> "New Class"
                            }
                        )
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .testTag("main_add_fab")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Screen router
            AnimatedContent(
                targetState = activeTab,
                transitionSpec = {
                    fadeIn(animationSpec = tween(220)) togetherWith fadeOut(animationSpec = tween(180))
                }, label = "TabContent"
            ) { targetTab ->
                when (targetTab) {
                    0 -> DashboardTab(
                        viewModel = viewModel,
                        onAddHabit = { showHabitDialog = true },
                        onAddExpense = { showExpenseDialog = true }
                    )
                    1 -> CalendarSchedulerTab(
                        viewModel = viewModel,
                        onAddEvent = { showEventDialog = true },
                        onAddSubject = { showSubjectDialog = true }
                    )
                    2 -> NotesRemindersTab(
                        viewModel = viewModel,
                        onAddReminder = { showReminderDialog = true },
                        onAddNote = { showNoteDialog = true }
                    )
                    3 -> AiStudyAssistantTab(
                        viewModel = viewModel
                    )
                    4 -> AcademicFinanceTab(
                        viewModel = viewModel,
                        onAddCourse = { showCourseDialog = true },
                        onAddExpense = { showExpenseDialog = true }
                    )
                }
            }
        }
    }

    // Modal popup instantiations
    if (showEventDialog) {
        TimetableEventEditorDialog(
            onDismiss = { showEventDialog = false },
            onSave = { day, subject, time, location, color ->
                viewModel.addTimetableEvent(day, subject, time, location, color)
                showEventDialog = false
            }
        )
    }

    if (showSubjectDialog) {
        AttendanceSubjectDialog(
            onDismiss = { showSubjectDialog = false },
            onSave = { name, count, total ->
                viewModel.addAttendanceSubject(name, count, total)
                showSubjectDialog = false
            }
        )
    }

    if (showReminderDialog) {
        TaskReminderDialog(
            onDismiss = { showReminderDialog = false },
            onSave = { title, desc, date, type ->
                viewModel.addReminder(title, desc, date, type)
                showReminderDialog = false
            }
        )
    }

    if (showNoteDialog) {
        NoteCreatorDialog(
            onDismiss = { showNoteDialog = false },
            onSave = { title, content, attachment, image ->
                viewModel.addNote(title, content, attachment, image)
                showNoteDialog = false
            }
        )
    }

    if (showHabitDialog) {
        HabitEditorDialog(
            onDismiss = { showHabitDialog = false },
            onSave = { name, freq ->
                viewModel.addHabit(name, freq)
                showHabitDialog = false
            }
        )
    }

    if (showExpenseDialog) {
        ExpenseEditorDialog(
            onDismiss = { showExpenseDialog = false },
            onSave = { desc, amount, category ->
                viewModel.addExpense(desc, amount, category)
                showExpenseDialog = false
            }
        )
    }

    if (showCourseDialog) {
        CourseEditorDialog(
            onDismiss = { showCourseDialog = false },
            onSave = { name, sem, grade, credits ->
                viewModel.addCourse(name, sem, grade, credits)
                showCourseDialog = false
            }
        )
    }
}

// ======================== TAB 0: DASHBOARD & MOTIVATION ========================
@Composable
fun DashboardTab(
    viewModel: StudentViewModel,
    onAddHabit: () -> Unit,
    onAddExpense: () -> Unit
) {
    val context = LocalContext.current
    val habits by viewModel.habitsList.collectAsStateWithLifecycle()
    val reminders by viewModel.remindersList.collectAsStateWithLifecycle()
    val attendance by viewModel.attendanceList.collectAsStateWithLifecycle()
    val isDarkTheme by viewModel.isDarkTheme.collectAsStateWithLifecycle()

    val pomodoroMode by viewModel.pomodoroMode.collectAsStateWithLifecycle()
    val timeRemaining by viewModel.pomodoroTimeRemaining.collectAsStateWithLifecycle()
    val totalSecs by viewModel.pomodoroTotalDuration.collectAsStateWithLifecycle()
    val isTimerActive by viewModel.isPomodoroRunning.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Welcome Header & Theme Switcher
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Hello, Student! 👋",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Commit to goals. Rule the semester.",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                }

                IconButton(
                    onClick = { viewModel.toggleDarkTheme() },
                    modifier = Modifier
                        .background(
                            MaterialTheme.colorScheme.surface,
                            shape = CircleShape
                        )
                        .glassCardBorder(CircleShape, isDarkTheme)
                ) {
                    Icon(
                        imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                        contentDescription = "Theme",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        // Pomodoro Widgets Glassmorphic Timer Box
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .glassCardBorder(RoundedCornerShape(24.dp), isDarkTheme),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Timer,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Pomodoro Study Engine",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }

                        // Preset switches
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            AssistChip(
                                onClick = { viewModel.setPomodoroMode("Work", 1500) },
                                label = { Text("Work", fontSize = 11.sp) }
                            )
                            AssistChip(
                                onClick = { viewModel.setPomodoroMode("Break", 300) },
                                label = { Text("Break", fontSize = 11.sp) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    val mins = timeRemaining / 60
                    val secs = timeRemaining % 60
                    val timerStr = String.format("%02d:%02d", mins, secs)

                    // Draw circular visual progress
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(160.dp)
                    ) {
                        val progressLeft = animateFloatAsState(
                            targetValue = if (totalSecs > 0) timeRemaining.toFloat() / totalSecs else 1f,
                            animationSpec = tween(500), label = "circle"
                        )

                        val trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                        val primaryColor = MaterialTheme.colorScheme.primary

                        Canvas(modifier = Modifier.fillMaxSize()) {
                            drawArc(
                                color = trackColor,
                                startAngle = -90f,
                                sweepAngle = 360f,
                                useCenter = false,
                                style = Stroke(width = 10.dp.toPx(), cap = StrokeCap.Round)
                            )
                            drawArc(
                                color = primaryColor,
                                startAngle = -90f,
                                sweepAngle = 360f * progressLeft.value,
                                useCenter = false,
                                style = Stroke(width = 10.dp.toPx(), cap = StrokeCap.Round)
                            )
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = timerStr,
                                fontSize = 34.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = pomodoroMode.uppercase(),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 2.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Buttons control
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = { viewModel.togglePomodoro() },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                imageVector = if (isTimerActive) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(if (isTimerActive) "Pause" else "Focus Now")
                        }

                        OutlinedButton(
                            onClick = { viewModel.resetPomodoro() },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Reset")
                        }
                    }
                }
            }
        }

        // Daily Streaks Habit Tracker Section
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF10B981)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Habits & Routine Goals",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                TextButton(onClick = onAddHabit) {
                    Text("+ Add")
                }
            }
        }

        if (habits.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth()
                        .glassCardBorder(RoundedCornerShape(16.dp), isDarkTheme),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "No active habits",
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Build academic consistency. Click add to configure a habit.",
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        } else {
            items(habits) { hb ->
                Card(
                    modifier = Modifier.fillMaxWidth()
                        .glassCardBorder(RoundedCornerShape(16.dp), isDarkTheme),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = hb.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = "Frequency: ${hb.frequency} | Streak: 🔥 ${hb.streak} Days",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = { viewModel.checkInHabit(hb.id) },
                                modifier = Modifier
                                    .background(
                                        Color(0xFF10B981).copy(alpha = 0.15f),
                                        shape = CircleShape
                                    )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Check-in",
                                    tint = Color(0xFF10B981)
                                )
                            }

                            IconButton(
                                onClick = { viewModel.deleteHabit(hb.id) }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DeleteOutline,
                                    contentDescription = "Delete",
                                    tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Attendance Overview & Statistics Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Percent,
                    contentDescription = null,
                    tint = Color(0xFFF59E0B)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Critical Attendance Tracker",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        if (attendance.isEmpty()) {
            item {
                Text(
                    "Track class attendances under the Calendar page.",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    modifier = Modifier.padding(bottom = 20.dp)
                )
            }
        } else {
            items(attendance) { att ->
                val ratio = if (att.totalClasses > 0) att.attendedClasses.toDouble() / att.totalClasses else 1.0
                val percent = ratio * 100
                val isBelowLimit = percent < 75.0

                Card(
                    modifier = Modifier.fillMaxWidth()
                        .glassCardBorder(RoundedCornerShape(16.dp), isDarkTheme),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                att.subjectName,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = String.format("%.1f%%", percent),
                                fontWeight = FontWeight.Bold,
                                color = if (isBelowLimit) MaterialTheme.colorScheme.error else Color(0xFF10B981)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Linear progress indicator
                        LinearProgressIndicator(
                            progress = ratio.toFloat(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = if (isBelowLimit) MaterialTheme.colorScheme.error else Color(0xFF10B981),
                            trackColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f)
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Attended: ${att.attendedClasses} / ${att.totalClasses} classes " +
                                    (if (isBelowLimit) "⚠️ Below standard 75.0% threshold." else "👍 Compliant ratio"),
                            fontSize = 11.sp,
                            color = if (isBelowLimit) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                        )
                    }
                }
            }
        }
    }
}

// ======================== TAB 1: CALENDAR & ATTENDANCE ========================
@Composable
fun CalendarSchedulerTab(
    viewModel: StudentViewModel,
    onAddEvent: () -> Unit,
    onAddSubject: () -> Unit
) {
    val timetable by viewModel.timetableEvents.collectAsStateWithLifecycle()
    val attendance by viewModel.attendanceList.collectAsStateWithLifecycle()
    val isDarkTheme by viewModel.isDarkTheme.collectAsStateWithLifecycle()

    var selectedDayFilter by remember { mutableStateOf("Monday") }
    val days = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column {
                Text(
                    "Dynamic timetable scheduler",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    "Switch days to preview class slots & faculty meetings",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
            }
        }

        // Horizontal slider control
        item {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(days) { d ->
                    val active = selectedDayFilter == d
                    FilterChip(
                        selected = active,
                        onClick = { selectedDayFilter = d },
                        label = { Text(d, fontSize = 13.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }

        // Timetable listings
        val filteredList = timetable.filter { it.dayOfWeek.equals(selectedDayFilter, ignoreCase = true) }

        if (filteredList.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth()
                        .glassCardBorder(RoundedCornerShape(16.dp), isDarkTheme),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Free day! 🎉 No lecture slots",
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                        )
                    }
                }
            }
        } else {
            items(filteredList) { ev ->
                // parse custom hex Safely
                val cardAccent = try {
                    Color(android.graphics.Color.parseColor(ev.colorHex))
                } catch (e: Exception) {
                    MaterialTheme.colorScheme.primary
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, cardAccent.copy(alpha = 0.4f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(cardAccent.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.School,
                                    tint = cardAccent,
                                    contentDescription = null
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Text(
                                    ev.subject,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                Text(
                                    "${ev.time} | ${ev.location}",
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                                )
                            }
                        }

                        IconButton(
                            onClick = { viewModel.deleteTimetableEvent(ev.id) }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Delete event",
                                tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                            )
                        }
                    }
                }
            }
        }

        // Attendance list Section
        item {
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Attendance Logs (Threshold 75%)",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                TextButton(onClick = onAddSubject) {
                    Text("+ Add Subject")
                }
            }
        }

        if (attendance.isEmpty()) {
            item {
                Text(
                    "No subjects stored. Click Add Subject to start calculating limits.",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                )
            }
        } else {
            items(attendance) { att ->
                val ratio = if (att.totalClasses > 0) att.attendedClasses.toDouble() / att.totalClasses else 1.0
                val percent = ratio * 100
                val below75 = percent < 75.0

                Card(
                    modifier = Modifier.fillMaxWidth()
                        .glassCardBorder(RoundedCornerShape(16.dp), isDarkTheme),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column {
                                Text(
                                    att.subjectName,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                Text(
                                    "Rate: ${att.attendedClasses} / ${att.totalClasses} lectures completed",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                                )
                            }

                            Text(
                                String.format("%.1f%%", percent),
                                fontWeight = FontWeight.Bold,
                                color = if (below75) MaterialTheme.colorScheme.error else Color(0xFF10B981)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Controls
                        Row(
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextButton(onClick = { viewModel.resetAttendance(att.id) }) {
                                Text("Reset")
                            }

                            IconButton(
                                onClick = { viewModel.adjustAttendance(att.id, false, false) }
                            ) {
                                Icon(Icons.Default.Remove, contentDescription = "Decrement")
                            }

                            IconButton(
                                onClick = { viewModel.adjustAttendance(att.id, true, true) }
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Attended")
                            }

                            IconButton(
                                onClick = { viewModel.adjustAttendance(att.id, false, true) }
                            ) {
                                Icon(Icons.Outlined.Cancel, contentDescription = "Missed class", tint = MaterialTheme.colorScheme.error.copy(alpha = 0.8f))
                            }

                            IconButton(
                                onClick = { viewModel.deleteAttendance(att.id) }
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete subject", tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f))
                            }
                        }
                    }
                }
            }
        }
    }
}

// ======================== TAB 2: REMINDERS & NOTES STORAGE ========================
@Composable
fun NotesRemindersTab(
    viewModel: StudentViewModel,
    onAddReminder: () -> Unit,
    onAddNote: () -> Unit
) {
    val reminders by viewModel.remindersList.collectAsStateWithLifecycle()
    val notes by viewModel.notesList.collectAsStateWithLifecycle()
    val isDarkTheme by viewModel.isDarkTheme.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Headers Reminders
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "Reminders System",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        "Exams dates and due assignments",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                }

                TextButton(onClick = onAddReminder) {
                    Text("+ Add Reminder")
                }
            }
        }

        val incompleteReminders = reminders.filter { !it.isCompleted }
        val completedReminders = reminders.filter { it.isCompleted }

        if (incompleteReminders.isEmpty() && completedReminders.isEmpty()) {
            item {
                Text(
                    "Excellent! No pending academic deadlines.",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                )
            }
        } else {
            // Pending items
            items(incompleteReminders) { task ->
                ReminderRow(task = task, viewModel = viewModel)
            }

            if (completedReminders.isNotEmpty()) {
                item {
                    Text("Completed Deadlines", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = MaterialTheme.colorScheme.primary)
                }
                items(completedReminders) { task ->
                    ReminderRow(task = task, viewModel = viewModel)
                }
            }
        }

        // Notes shelf Section
        item {
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Notes & Files Drawer",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                TextButton(onClick = onAddNote) {
                    Text("+ Add Note")
                }
            }
        }

        if (notes.isEmpty()) {
            item {
                Text(
                    "Store revision notes or mock questions. Supports simulated attachments.",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                )
            }
        } else {
            items(notes) { nt ->
                Card(
                    modifier = Modifier.fillMaxWidth()
                        .glassCardBorder(RoundedCornerShape(16.dp), isDarkTheme),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                nt.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.onBackground
                            )

                            IconButton(
                                onClick = { viewModel.deleteNote(nt.id) },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Remove note",
                                    tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            nt.content,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                            maxLines = 5,
                            overflow = TextOverflow.Ellipsis
                        )

                        // Attachments indications
                        if (!nt.attachmentPath.isNullOrEmpty() || !nt.imagePath.isNullOrEmpty()) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                if (!nt.attachmentPath.isNullOrEmpty()) {
                                    AssistChip(
                                        onClick = {},
                                        leadingIcon = { Icon(Icons.Default.PictureAsPdf, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.Red) },
                                        label = { Text("PDF: ${nt.attachmentPath}", fontSize = 11.sp) }
                                    )
                                }
                                if (!nt.imagePath.isNullOrEmpty()) {
                                    AssistChip(
                                        onClick = {},
                                        leadingIcon = { Icon(Icons.Default.Image, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.Blue) },
                                        label = { Text("Image: ${nt.imagePath}", fontSize = 11.sp) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ReminderRow(task: Reminder, viewModel: StudentViewModel) {
    val formatter = remember { SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault()) }
    val dateStr = formatter.format(Date(task.dueDate))
    val isDarkTheme by viewModel.isDarkTheme.collectAsStateWithLifecycle()

    Card(
        modifier = Modifier.fillMaxWidth()
            .glassCardBorder(RoundedCornerShape(12.dp), isDarkTheme),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (task.isCompleted) MaterialTheme.colorScheme.surface.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Checkbox(
                    checked = task.isCompleted,
                    onCheckedChange = { viewModel.toggleReminderCompleted(task.id) }
                )

                Spacer(modifier = Modifier.width(6.dp))

                Column {
                    Text(
                        text = task.title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (task.isCompleted) MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f) else MaterialTheme.colorScheme.onBackground,
                        textDecoration = if (task.isCompleted) androidx.compose.ui.text.style.TextDecoration.LineThrough else null
                    )
                    Text(
                        text = "${task.type} | Due: $dateStr",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            IconButton(onClick = { viewModel.deleteReminder(task.id) }) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Remove deadline",
                    tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
                )
            }
        }
    }
}

// ======================== TAB 3: AI STUDY ASSISTANT ========================
@Composable
fun AiStudyAssistantTab(
    viewModel: StudentViewModel
) {
    val chatHistory by viewModel.chatMessages.collectAsStateWithLifecycle()
    val isAiLoading by viewModel.isAiLoading.collectAsStateWithLifecycle()
    val generatedPlan by viewModel.aiGeneratedPlan.collectAsStateWithLifecycle()
    val isDarkTheme by viewModel.isDarkTheme.collectAsStateWithLifecycle()

    var textInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column {
            Text(
                "Gemini AI Study Assistant",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                "Prompt templates, instant solutions, or auto-scheduler planners",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
        }

        // AI study schedule launcher card
        Card(
            modifier = Modifier.fillMaxWidth()
                .glassCardBorder(RoundedCornerShape(20.dp), isDarkTheme),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "AI Weekly Study Planner Generator",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    "Autocompiles your active academic courses and habits list into a tailored hour-by-hour weekly exam preparation plan.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { viewModel.generateAiStudyPlan() },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !isAiLoading
                ) {
                    Text("Auto-Generate Now & Sync to Notes", color = Color.White)
                }
            }
        }

        // Suggestions template chips
        Text("Quick Prompts:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            val suggestions = listOf(
                "Draft weekly revision guidelines",
                "Break down complex Calculus concepts",
                "Explain SGPA limits calculation",
                "Prepare focus intervals recommendations"
            )
            items(suggestions) { q ->
                AssistChip(
                    onClick = { viewModel.sendChatMessage(q) },
                    label = { Text(q, fontSize = 11.sp) }
                )
            }
        }

        // Chat logs viewport
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surface)
                .glassCardBorder(RoundedCornerShape(16.dp), isDarkTheme)
        ) {
            if (chatHistory.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.QuestionAnswer,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        "No chats started yet",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "Ask questions about courses, plan calendars or college budget categories.",
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
                    )
                }
            } else {
                val scrollState = rememberScrollState()
                LaunchedEffect(chatHistory.size, isAiLoading) {
                    scrollState.animateScrollTo(scrollState.maxValue)
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    for (msg in chatHistory) {
                        val isUser = msg.sender == "user"
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(
                                        RoundedCornerShape(
                                            topStart = 16.dp,
                                            topEnd = 16.dp,
                                            bottomStart = if (isUser) 16.dp else 4.dp,
                                            bottomEnd = if (isUser) 4.dp else 16.dp
                                        )
                                    )
                                    .background(
                                        if (isUser) MaterialTheme.colorScheme.primary
                                        else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.06f)
                                    )
                                    .padding(horizontal = 14.dp, vertical = 10.dp)
                                    .widthIn(max = 260.dp)
                            ) {
                                Text(
                                    text = msg.text,
                                    fontSize = 14.sp,
                                    color = if (isUser) Color.White else MaterialTheme.colorScheme.onBackground
                                )
                            }
                        }
                    }

                    if (isAiLoading) {
                        Row(
                            horizontalArrangement = Arrangement.Start,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.04f))
                                    .padding(horizontal = 14.dp, vertical = 10.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(16.dp),
                                        strokeWidth = 2.dp,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Gemini is composing...", fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Input Console Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = textInput,
                onValueChange = { textInput = it },
                placeholder = { Text("Ask Gemini study advice...") },
                modifier = Modifier
                    .weight(1f)
                    .testTag("chat_input"),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                maxLines = 3
            )

            IconButton(
                onClick = {
                    if (textInput.isNotBlank()) {
                        viewModel.sendChatMessage(textInput)
                        textInput = ""
                    }
                },
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary, CircleShape)
                    .size(48.dp)
                    .testTag("send_chat_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send",
                    tint = Color.White
                )
            }

            IconButton(
                onClick = { viewModel.clearChat() },
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.05f), CircleShape)
                    .size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.DeleteSweep,
                    contentDescription = "Clear History",
                    tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
            }
        }
    }
}

// ======================== TAB 4: SEMESTER ACADEMICS & WALLET ========================
@Composable
fun AcademicFinanceTab(
    viewModel: StudentViewModel,
    onAddCourse: () -> Unit,
    onAddExpense: () -> Unit
) {
    val courses by viewModel.coursesList.collectAsStateWithLifecycle()
    val expenses by viewModel.expensesList.collectAsStateWithLifecycle()
    val isDarkTheme by viewModel.isDarkTheme.collectAsStateWithLifecycle()

    val aggregateCgpa = viewModel.calculateCgpa()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Headers CGPA
        item {
            Column {
                Text(
                    "Academics & CGPA Calculator",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    "Store grades across semesters with auto weight calculations",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
            }
        }

        // GPA Aggregate Dashboard card
        item {
            Card(
                modifier = Modifier.fillMaxWidth()
                    .glassCardBorder(RoundedCornerShape(20.dp), isDarkTheme),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            "Cumulative CGPA",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = String.format("%.2f / 4.00", aggregateCgpa),
                            color = Color.White,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .background(Color.White.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = if (aggregateCgpa >= 3.5) "First Class 🏆" else "Active Semester",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // Course logs with delete option
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Enrolled Classes", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                TextButton(onClick = onAddCourse) {
                    Text("+ Add Course")
                }
            }
        }

        if (courses.isEmpty()) {
            item {
                Text("No courses added yet. Save semesters courses to compute average.", fontSize = 13.sp, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f))
            }
        } else {
            items(courses) { c ->
                Card(
                    modifier = Modifier.fillMaxWidth()
                        .glassCardBorder(RoundedCornerShape(12.dp), isDarkTheme),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("S${c.semester}", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, fontSize = 12.sp)
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(c.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text("${c.credits} Credits | Weight: ${viewModel.getValueForGradeAsString(c.grade)} pts", fontSize = 12.sp, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f))
                            }
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .background(Color(0xFF10B981).copy(alpha = 0.15f), RoundedCornerShape(6.dp))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(c.grade, color = Color(0xFF047857), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }

                            IconButton(onClick = { viewModel.deleteCourse(c.id) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Remove course", tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f), modifier = Modifier.size(20.dp))
                            }
                        }
                    }
                }
            }
        }

        // Finance / Hostel expenses Section
        item {
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Hostel & School Expenses", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text("Keep budget categories tracked", fontSize = 12.sp, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f))
                }

                TextButton(onClick = onAddExpense) {
                    Text("+ Add Expense")
                }
            }
        }

        val totalExpenses = expenses.sumOf { it.amount }

        item {
            Card(
                modifier = Modifier.fillMaxWidth()
                    .glassCardBorder(RoundedCornerShape(16.dp), isDarkTheme),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Total Outgoings", fontWeight = FontWeight.Bold)
                    Text(
                        text = String.format("$%.2f", totalExpenses),
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 18.sp
                    )
                }
            }
        }

        if (expenses.isEmpty()) {
            item {
                Text("Wallet is empty. Track leisure, food, hostel, or academics spending.", fontSize = 13.sp, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f))
            }
        } else {
            items(expenses) { ex ->
                Card(
                    modifier = Modifier.fillMaxWidth()
                        .glassCardBorder(RoundedCornerShape(12.dp), isDarkTheme),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(ex.description, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("Category: ${ex.category}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f))
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(String.format("$%.2f", ex.amount), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(8.dp))
                            IconButton(onClick = { viewModel.deleteExpense(ex.id) }) {
                                Icon(Icons.Default.DeleteOutline, contentDescription = "Delete outgoings", tint = MaterialTheme.colorScheme.error.copy(alpha = 0.8f))
                            }
                        }
                    }
                }
            }
        }
    }
}

// Extension to avoid compilation error about missing method
fun StudentViewModel.getValueForGradeAsString(grade: String): String {
    return String.format("%.1f", this.getGradeValue(grade))
}


// ======================== MODAL DIALOGS COMPILATION ========================

@Composable
fun TimetableEventEditorDialog(
    onDismiss: () -> Unit,
    onSave: (day: String, subject: String, time: String, location: String, color: String) -> Unit
) {
    var subject by remember { mutableStateOf("") }
    var day by remember { mutableStateOf("Monday") }
    var time by remember { mutableStateOf("09:00 AM - 10:30 AM") }
    var rName by remember { mutableStateOf("Classroom 101") }

    val colors = listOf("#8B5CF6", "#10B981", "#F59E0B", "#3B82F6", "#EC4899")
    var selectedHex by remember { mutableStateOf("#8B5CF6") }

    val days = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday")

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Schedule New Lecture", fontSize = 18.sp, fontWeight = FontWeight.Bold)

                OutlinedTextField(
                    value = subject,
                    onValueChange = { subject = it },
                    label = { Text("Course Title") },
                    modifier = Modifier.fillMaxWidth().testTag("add_event_subject_input")
                )

                Text("Day of Week:")
                Row(modifier = Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    days.forEach { dy ->
                        val active = dy == day
                        FilterChip(
                            selected = active,
                            onClick = { day = dy },
                            label = { Text(dy) }
                        )
                    }
                }

                OutlinedTextField(
                    value = time,
                    onValueChange = { time = it },
                    label = { Text("Timing slots (e.g., 02:00 PM - 03:30 PM)") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = rName,
                    onValueChange = { rName = it },
                    label = { Text("Seminar Room / Location") },
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Label Theme:")
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    for (c in colors) {
                        val active = c == selectedHex
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(Color(android.graphics.Color.parseColor(c)))
                                .clickable { selectedHex = c }
                                .border(if (active) 3.dp else 0.dp, MaterialTheme.colorScheme.onBackground, CircleShape)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) { Text("Dismiss") }
                    Button(
                        onClick = {
                            if (subject.isNotBlank()) {
                                onSave(day, subject, time, rName, selectedHex)
                            }
                        },
                        enabled = subject.isNotBlank(),
                        modifier = Modifier.testTag("add_event_save_button")
                    ) {
                        Text("Save Entry")
                    }
                }
            }
        }
    }
}

@Composable
fun AttendanceSubjectDialog(
    onDismiss: () -> Unit,
    onSave: (name: String, attended: Int, total: Int) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var countStr by remember { mutableStateOf("0") }
    var totalStr by remember { mutableStateOf("0") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text("Add New Attendance Tracker", fontWeight = FontWeight.Bold, fontSize = 18.sp)

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Name (e.g. Physics)") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = countStr,
                    onValueChange = { countStr = it },
                    label = { Text("Attended Lectures") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = totalStr,
                    onValueChange = { totalStr = it },
                    label = { Text("Total Lectures") },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    Button(
                        onClick = {
                            if (title.isNotBlank()) {
                                val count = countStr.toIntOrNull() ?: 0
                                val tot = totalStr.toIntOrNull() ?: 0
                                onSave(title, count, tot)
                            }
                        },
                        enabled = title.isNotBlank()
                    ) {
                        Text("Save Tracker")
                    }
                }
            }
        }
    }
}

@Composable
fun TaskReminderDialog(
    onDismiss: () -> Unit,
    onSave: (title: String, desc: String, date: Long, type: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("Assignment") } // "Assignment" or "Exam"

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Record Academic Deadline", fontWeight = FontWeight.Bold, fontSize = 18.sp)

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = desc,
                    onValueChange = { desc = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Type:")
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    FilterChip(
                        selected = type == "Assignment",
                        onClick = { type = "Assignment" },
                        label = { Text("Assignment") }
                    )
                    FilterChip(
                        selected = type == "Exam",
                        onClick = { type = "Exam" },
                        label = { Text("Exam") }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    Button(
                        onClick = {
                            if (title.isNotBlank()) {
                                // Default due date 5 days and 1 hour elements in advance for demo
                                onSave(title, desc, System.currentTimeMillis() + 432000000L, type)
                            }
                        },
                        enabled = title.isNotBlank()
                    ) {
                        Text("Save Deadline")
                    }
                }
            }
        }
    }
}

@Composable
fun NoteCreatorDialog(
    onDismiss: () -> Unit,
    onSave: (title: String, content: String, attachment: String?, image: String?) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var body by remember { mutableStateOf("") }

    var attachPdf by remember { mutableStateOf(false) }
    var attachImg by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Draft Notebook Note", fontWeight = FontWeight.Bold, fontSize = 18.sp)

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Note Title") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = body,
                    onValueChange = { body = it },
                    label = { Text("Take bullet suggestions...") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 4
                )

                Text("Simulate PDF/Image attachments indicators (Offline Mock):")
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = attachPdf,
                        onClick = { attachPdf = !attachPdf },
                        label = { Text("Attach PDF") }
                    )
                    FilterChip(
                        selected = attachImg,
                        onClick = { attachImg = !attachImg },
                        label = { Text("Attach Photo") }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) { Text("Dismiss") }
                    Button(
                        onClick = {
                            if (title.isNotBlank()) {
                                onSave(
                                    title,
                                    body,
                                    if (attachPdf) "Lecture_Slide_Final.pdf" else null,
                                    if (attachImg) "Curriculum_Capture_01.jpg" else null
                                )
                            }
                        },
                        enabled = title.isNotBlank()
                    ) {
                        Text("Draft Note")
                    }
                }
            }
        }
    }
}

@Composable
fun HabitEditorDialog(
    onDismiss: () -> Unit,
    onSave: (name: String, freq: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var freq by remember { mutableStateOf("Daily") } // "Daily" or "Weekly"

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Create Habit Goal", fontWeight = FontWeight.Bold, fontSize = 18.sp)

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Routine title (e.g., Code for 1hr)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Routine frequency:")
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    FilterChip(
                        selected = freq == "Daily",
                        onClick = { freq = "Daily" },
                        label = { Text("Daily") }
                    )
                    FilterChip(
                        selected = freq == "Weekly",
                        onClick = { freq = "Weekly" },
                        label = { Text("Weekly") }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) { Text("Dismiss") }
                    Button(
                        onClick = {
                            if (title.isNotBlank()) {
                                onSave(title, freq)
                            }
                        },
                        enabled = title.isNotBlank()
                    ) {
                        Text("Commit Goal")
                    }
                }
            }
        }
    }
}

@Composable
fun ExpenseEditorDialog(
    onDismiss: () -> Unit,
    onSave: (desc: String, valAmt: Double, category: String) -> Unit
) {
    var item by remember { mutableStateOf("") }
    var cash by remember { mutableStateOf("") }
    var cat by remember { mutableStateOf("Food") }

    val categories = listOf("Food", "Hostel", "Books", "Leisure", "Misc")

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("File Student Spending Outgoings", fontWeight = FontWeight.Bold, fontSize = 18.sp)

                OutlinedTextField(
                    value = item,
                    onValueChange = { item = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = cash,
                    onValueChange = { cash = it },
                    label = { Text("Amount ($)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Category:")
                Row(modifier = Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    for (c in categories) {
                        FilterChip(
                            selected = cat == c,
                            onClick = { cat = c },
                            label = { Text(c) }
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) { Text("Discard") }
                    Button(
                        onClick = {
                            if (item.isNotBlank()) {
                                val amt = cash.toDoubleOrNull() ?: 0.0
                                onSave(item, amt, cat)
                            }
                        },
                        enabled = item.isNotBlank()
                    ) {
                        Text("Log cash")
                    }
                }
            }
        }
    }
}

@Composable
fun CourseEditorDialog(
    onDismiss: () -> Unit,
    onSave: (name: String, semester: Int, grade: String, credits: Int) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var semesterStr by remember { mutableStateOf("1") }
    var grade by remember { mutableStateOf("A") }
    var creditsStr by remember { mutableStateOf("3") }

    val grades = listOf("A+", "A", "A-", "B+", "B", "B-", "C+", "C", "D", "F")

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Record Completed Semester Course", fontWeight = FontWeight.Bold, fontSize = 18.sp)

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Course Name (e.g., Stats 101)") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = semesterStr,
                    onValueChange = { semesterStr = it },
                    label = { Text("Semester (numeric)") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = creditsStr,
                    onValueChange = { creditsStr = it },
                    label = { Text("Course Credits Weight") },
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Obtained Grade:")
                Row(modifier = Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    for (g in grades) {
                        FilterChip(
                            selected = grade == g,
                            onClick = { grade = g },
                            label = { Text(g) }
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) { Text("Discard") }
                    Button(
                        onClick = {
                            if (title.isNotBlank()) {
                                onSave(title, semesterStr.toIntOrNull() ?: 1, grade, creditsStr.toIntOrNull() ?: 3)
                            }
                        },
                        enabled = title.isNotBlank()
                    ) {
                        Text("Record Course")
                    }
                }
            }
        }
    }
}

// ======================== GLASSMORPHISM HELPERS ========================
fun Modifier.glowingGlassBackground(isDark: Boolean): Modifier = this.drawBehind {
    val topBlobColor = if (isDark) Color(0xFF6366F1).copy(alpha = 0.16f) else Color(0xFFC7D2FE).copy(alpha = 0.65f) // indigo-200 / indigo opacity
    val centerBlobColor = if (isDark) Color(0xFFA855F7).copy(alpha = 0.14f) else Color(0xFFE9D5FF).copy(alpha = 0.55f) // purple-200 / purple opacity
    val bottomBlobColor = if (isDark) Color(0xFF3B82F6).copy(alpha = 0.14f) else Color(0xFFDBEAFE).copy(alpha = 0.50f) // blue-200 / blue opacity

    // 1st Blob (Top-Right)
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(topBlobColor, Color.Transparent),
            center = Offset(x = size.width + 100f, y = -100f),
            radius = size.width * 0.75f
        ),
        radius = size.width * 0.75f,
        center = Offset(x = size.width + 100f, y = -100f)
    )

    // 2nd Blob (Center-Left)
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(centerBlobColor, Color.Transparent),
            center = Offset(x = -150f, y = size.height * 0.5f),
            radius = size.width * 0.85f
        ),
        radius = size.width * 0.85f,
        center = Offset(x = -150f, y = size.height * 0.5f)
    )

    // 3rd Blob (Bottom-Right)
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(bottomBlobColor, Color.Transparent),
            center = Offset(x = size.width * 0.75f, y = size.height + 100f),
            radius = size.width * 0.8f
        ),
        radius = size.width * 0.8f,
        center = Offset(x = size.width * 0.75f, y = size.height + 100f)
    )
}

fun Modifier.glassCardBorder(shape: androidx.compose.ui.graphics.Shape, isDark: Boolean): Modifier {
    return this.border(
        width = 1.dp,
        color = Color.White.copy(alpha = if (isDark) 0.15f else 0.80f),
        shape = shape
    )
}

