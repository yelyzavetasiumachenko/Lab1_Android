package com.example.lab1.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.lab1.model.*
import androidx.compose.ui.Alignment

@Composable
fun HorizontalItem(
    horizontalList: HorizontalTutorsList,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = "👨‍🏫 Провідні викладачі:",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = horizontalList.tutors,
                key = { tutor -> "h_${tutor.id}" }
            ) { tutor ->
                Surface(
                    modifier = Modifier.width(135.dp),
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                    tonalElevation = 0.dp
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Surface(
                            shape = androidx.compose.foundation.shape.CircleShape,
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.size(50.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = tutor.name.take(1),
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = tutor.name,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold,
                            maxLines = 1,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = tutor.role,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2,
                            minLines = 2,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}