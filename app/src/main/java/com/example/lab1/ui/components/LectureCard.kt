package com.example.lab1.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.lab1.model.*
import androidx.compose.ui.Alignment

@Composable
fun LectureCard(lecture: Lecture, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
        ),
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Лекція №${lecture.id}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = lecture.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                lecture.tutor?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "👨‍🏫 ${it.name}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Surface(
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f),
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = "${lecture.durationMin} хв",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}