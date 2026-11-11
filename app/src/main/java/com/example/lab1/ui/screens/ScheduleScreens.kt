package com.example.lab1.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// ПІДЕКРАН 1
@Composable
fun ScheduleListSubScreen(
    modifier: Modifier = Modifier,
    onNavigateToDetails: (String, Int) -> Unit
) {
    val topics = listOf("Введення у JavaScript", "Об'єкт. Масиви. Об'єкт Array", "Опрацювання подій", "Browser Object Model", "Document Object Model")
    var currentIndex by rememberSaveable { mutableIntStateOf(0) }

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Курс: Основи мови програмування JavaScript",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 24.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Text(
            text = "Лекція ${currentIndex + 1}: ${topics[currentIndex]}",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(onClick = { currentIndex-- }, enabled = currentIndex > 0) {
                Text("Попередня")
            }

            Button(onClick = { currentIndex++ }, enabled = currentIndex < topics.size - 1) {
                Text("Наступна")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { onNavigateToDetails(topics[currentIndex], currentIndex + 1) },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
        ) {
            Text("Детальніше про лекцію")
        }
    }
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