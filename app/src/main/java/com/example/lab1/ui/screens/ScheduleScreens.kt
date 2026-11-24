package com.example.lab1.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.foundation.lazy.LazyColumn
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lab1.viewmodel.ScheduleViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.style.TextAlign
import com.example.lab1.ui.components.*
import com.example.lab1.model.*

@Composable
fun ScheduleListSubScreen(
    modifier: Modifier = Modifier,
    onNavigateToDetails: (String, Int) -> Unit,
    viewModel: ScheduleViewModel = viewModel(factory = ScheduleViewModel.Factory)
) {
    val listData by viewModel.listData.collectAsState()
    val tutors by viewModel.tutors.collectAsState()
    val lecturesOnly = remember(listData) { listData.filterIsInstance<Lecture>() }

    // Стани для діалогів додавання / редагування
    var showTutorDialog by remember { mutableStateOf(false) }
    var editingTutor by remember { mutableStateOf<Tutor?>(null) }

    var showLectureDialog by remember { mutableStateOf(false) }
    var editingLecture by remember { mutableStateOf<Lecture?>(null) }

    // Стани для підтвердження видалення
    var tutorToDelete by remember { mutableStateOf<Tutor?>(null) }
    var lectureToDelete by remember { mutableStateOf<Lecture?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Верхня шапка курсу + кнопки додавання
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "💻 Онлайн-курс",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Основи JavaScript",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilledTonalButton(onClick = {
                        editingTutor = null
                        showTutorDialog = true
                    }) {
                        Text("+ Викладач")
                    }
                    Button(onClick = {
                        editingLecture = null
                        showLectureDialog = true
                    }) {
                        Text("+ Лекція")
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TextButton(onClick = { viewModel.restoreDefaultData() }) {
                        Text("🔄 Відновити базові")
                    }
                    TextButton(onClick = { viewModel.clearAllData() }) {
                        Text("🗑️ Очистити БД", color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = listData,
                key = { item ->
                    "${item.id}_${
                        when (item) {
                            is Tutor -> "tutor_${item.fullName}"
                            is Lecture -> "lecture_${item.title}"
                            is HorizontalTutorsList -> "horizontal_${item.tutors.size}"
                            is HeaderItem -> "header_${item.title}"
                        }
                    }"
                }
            ) { item ->
                when (item) {
                    is HorizontalTutorsList -> HorizontalItem(horizontalList = item)

                    is HeaderItem -> Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 4.dp, end = 4.dp, top = 16.dp, bottom = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    is Tutor -> TutorCard(
                        tutor = item,
                        onEdit = {
                            editingTutor = item
                            showTutorDialog = true
                        },
                        onDelete = { tutorToDelete = item },
                        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                    )

//                    is Lecture -> LectureCard(
//                        lecture = item,
//                        onClick = { onNavigateToDetails(item.title, item.id) },
//                        onEdit = {
//                            editingLecture = item
//                            showLectureDialog = true
//                        },
//                        onDelete = { lectureToDelete = item }
//                    )
                    is Lecture -> {
                        val lectureNumber = lecturesOnly.indexOf(item) + 1
                        LectureCard(
                            lecture = item,
                            lectureNumber = lectureNumber,
                            onClick = { onNavigateToDetails(item.title, item.id) },
                            onEdit = {
                                editingLecture = item
                                showLectureDialog = true
                            },
                            onDelete = { lectureToDelete = item }
                        )
                    }
                }
            }
        }
    }

    // Діалог додавання / редагування викладача
    if (showTutorDialog) {
        TutorFormDialog(
            initialTutor = editingTutor,
            onDismiss = { showTutorDialog = false },
            onConfirm = { firstName, lastName, role ->
                val current = editingTutor
                if (current == null) {
                    viewModel.addTutor(firstName, lastName, role)
                } else {
                    viewModel.updateTutor(
                        current.copy(
                            firstName = firstName,
                            lastName = lastName,
                            role = role
                        )
                    )
                }
                showTutorDialog = false
            }
        )
    }

    // Діалог додавання / редагування лекції
    if (showLectureDialog) {
        LectureFormDialog(
            initialLecture = editingLecture,
            tutors = tutors,
            onDismiss = { showLectureDialog = false },
            onConfirm = { title, duration, selectedTutor ->
                val current = editingLecture
                if (current == null) {
                    viewModel.addLecture(title, duration, selectedTutor?.id)
                } else {
                    viewModel.updateLecture(
                        current.copy(
                            title = title,
                            durationMin = duration,
                            tutor = selectedTutor
                        )
                    )
                }
                showLectureDialog = false
            }
        )
    }

    // Діалог підтвердження видалення викладача
    tutorToDelete?.let { tutor ->
        AlertDialog(
            onDismissRequest = { tutorToDelete = null },
            title = { Text("Видалити викладача?") },
            text = {
                Text(
                    "Ви дійсно хочете видалити викладача ${tutor.fullName}? " +
                            "Усі пов'язані з ним лекції також будуть видалені."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteTutor(tutor)
                        tutorToDelete = null
                    }
                ) {
                    Text("Видалити", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { tutorToDelete = null }) {
                    Text("Скасувати")
                }
            }
        )
    }

    // Діалог підтвердження видалення лекції
    lectureToDelete?.let { lecture ->
        AlertDialog(
            onDismissRequest = { lectureToDelete = null },
            title = { Text("Видалити лекцію?") },
            text = {
                Text("Ви дійсно хочете видалити лекцію «${lecture.title}»?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteLecture(lecture)
                        lectureToDelete = null
                    }
                ) {
                    Text("Видалити", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { lectureToDelete = null }) {
                    Text("Скасувати")
                }
            }
        )
    }
}

@Composable
private fun TutorFormDialog(
    initialTutor: Tutor?,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String) -> Unit
) {
    var lastName by remember { mutableStateOf(initialTutor?.lastName ?: "") }
    var firstName by remember { mutableStateOf(initialTutor?.firstName ?: "") }
    var role by remember { mutableStateOf(initialTutor?.role ?: "") }

    // Перевірка, чи заповнені всі поля
    val isFormValid = lastName.isNotBlank() && firstName.isNotBlank() && role.isNotBlank()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initialTutor == null) "Новий викладач" else "Редагувати викладача") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = { Text("Прізвище") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = { Text("Ім'я") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = role,
                    onValueChange = { role = it },
                    label = { Text("Посада / роль") },
                    singleLine = true
                )

                // Підказка, якщо не всі поля заповнені
                if (!isFormValid) {
                    Text(
                        text = "⚠️ Заповніть усі поля, щоб зберегти викладача",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                enabled = isFormValid,
                onClick = { onConfirm(firstName, lastName, role) }
            ) {
                Text("Зберегти")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Скасувати") }
        }
    )
}

@Composable
private fun LectureFormDialog(
    initialLecture: Lecture?,
    tutors: List<Tutor>,
    onDismiss: () -> Unit,
    onConfirm: (String, Int, Tutor?) -> Unit
) {
    var title by remember { mutableStateOf(initialLecture?.title ?: "") }
    var durationText by remember { mutableStateOf(initialLecture?.durationMin?.toString() ?: "45") }
    var selectedTutor by remember {
        mutableStateOf(initialLecture?.tutor ?: tutors.firstOrNull())
    }

    val duration = durationText.toIntOrNull()
    // Перевірка, чи заповнені назва, коректна тривалість та обраний викладач (якщо вони є)
    val isFormValid = title.isNotBlank() &&
            duration != null &&
            duration > 0 &&
            (tutors.isEmpty() || selectedTutor != null)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initialLecture == null) "Нова лекція" else "Редагувати лекцію") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Назва лекції") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = durationText,
                    onValueChange = { durationText = it.filter { ch -> ch.isDigit() } },
                    label = { Text("Тривалість (хв)") },
                    singleLine = true
                )
                if (tutors.isNotEmpty()) {
                    Text(
                        text = "Викладач:",
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    tutors.forEach { tutor ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedTutor = tutor }
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedTutor?.id == tutor.id,
                                onClick = { selectedTutor = tutor }
                            )
                            Text(
                                text = tutor.fullName,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

                // Підказка, якщо не всі поля заповнені
                if (!isFormValid) {
                    Text(
                        text = "⚠️ Заповніть усі поля (назву та тривалість > 0), щоб зберегти",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                enabled = isFormValid,
                onClick = {
                    if (duration != null) {
                        onConfirm(title, duration, selectedTutor)
                    }
                }
            ) {
                Text("Зберегти")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Скасувати") }
        }
    )
}


// ПІДЕКРАН 2
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LectureDetailsSubScreen(
    topicName: String,
    lectureNumber: Int,
    onNavigateBack: () -> Unit,
    onNavigateToResources: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Деталі лекції") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = "Лекція №$lectureNumber",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Тема: $topicName",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Тут знаходиться детальний опис лекції, завдання для самостійного опрацювання та посилання на додаткові матеріали.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { onNavigateToResources(topicName) },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
            ) {
                Text("Додаткові матеріали")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = onNavigateBack) {
                Text("Повернутися до списку")
            }
        }
    }
}

// ПІДЕКРАН 3
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LectureResourcesSubScreen(
    topicName: String,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Матеріали") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = topicName,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            Text(
                text = "Корисні посилання для теми:",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 10.dp)
            )

            // Заглушки для посилань
            Text("🔗 Офіційна документація")
            Spacer(modifier = Modifier.height(8.dp))
            Text("🔗 Відео-уроки на YouTube")
            Spacer(modifier = Modifier.height(8.dp))
            Text("🔗 Тести для самоперевірки")

            Spacer(modifier = Modifier.height(32.dp))

            Button(onClick = onNavigateBack) {
                Text("Назад до опису лекції")
            }
        }
    }
}