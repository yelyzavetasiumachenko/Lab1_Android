package com.example.lab1.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import androidx.compose.foundation.lazy.LazyColumn
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lab1.viewmodel.ScheduleViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.lab1.ui.components.*
import com.example.lab1.model.*

@Composable
fun ScheduleListSubScreen(
    modifier: Modifier = Modifier,
    onNavigateToDetails: (String, Int) -> Unit,
    viewModel: ScheduleViewModel = viewModel()
) {
    val listData by viewModel.listData.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.9f),
            border = androidx.compose.foundation.BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
            ),
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "💻 Онлайн-курс",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Основи JavaScript",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface
                )
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
                            is Tutor -> item.name
                            is Lecture -> item.title
                            is HorizontalTutorsList -> item.hashCode()
                            is HeaderItem -> item.title
                        }
                    }"
                }
            ) { item ->
                when (item) {
                    is HorizontalTutorsList -> HorizontalItem(horizontalList = item)

                    is HeaderItem -> {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = 16.dp,
                                    end = 16.dp,
                                    top = 26.dp,
                                    bottom = 0.dp
                                ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = item.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    is Tutor -> TutorCard(
                        tutor = item,
                        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                    )

                    is Lecture -> LectureCard(
                        lecture = item,
                        onClick = { onNavigateToDetails(item.title, item.id) }
                    )
                }
            }
        }
    }
}


// ПІДЕКРАН 1
//@Composable
//fun ScheduleListSubScreen(
//    modifier: Modifier = Modifier,
//    onNavigateToDetails: (String, Int) -> Unit
//) {
//    val topics = listOf("Введення у JavaScript", "Об'єкт. Масиви. Об'єкт Array", "Опрацювання подій", "Browser Object Model", "Document Object Model")
//    var currentIndex by rememberSaveable { mutableIntStateOf(0) }
//
//    Column(
//        modifier = modifier.fillMaxSize().padding(16.dp),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text(
//            text = "Курс: Основи мови програмування JavaScript",
//            style = MaterialTheme.typography.titleLarge,
//            modifier = Modifier.padding(bottom = 24.dp),
//            textAlign = androidx.compose.ui.text.style.TextAlign.Center
//        )
//
//        Text(
//            text = "Лекція ${currentIndex + 1}: ${topics[currentIndex]}",
//            style = MaterialTheme.typography.bodyLarge,
//            modifier = Modifier.padding(bottom = 32.dp)
//        )
//
//        Row(
//            horizontalArrangement = Arrangement.spacedBy(16.dp)
//        ) {
//            Button(onClick = { currentIndex-- }, enabled = currentIndex > 0) {
//                Text("Попередня")
//            }
//
//            Button(onClick = { currentIndex++ }, enabled = currentIndex < topics.size - 1) {
//                Text("Наступна")
//            }
//        }
//
//        Spacer(modifier = Modifier.height(32.dp))
//
//        Button(
//            onClick = { onNavigateToDetails(topics[currentIndex], currentIndex + 1) },
//            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
//        ) {
//            Text("Детальніше про лекцію")
//        }
//    }
//}

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