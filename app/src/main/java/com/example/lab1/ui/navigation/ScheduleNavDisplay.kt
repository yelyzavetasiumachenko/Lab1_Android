package com.example.lab1.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.lab1.ui.screens.LectureDetailsSubScreen
import com.example.lab1.ui.screens.LectureResourcesSubScreen
import com.example.lab1.ui.screens.ScheduleListSubScreen

@Composable
fun ScheduleNavDisplay(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = ScheduleScreenRoute.List,
        modifier = modifier
    ) {
        // Конфігурація підекрану 1
        composable<ScheduleScreenRoute.List> {
            ScheduleListSubScreen(
                onNavigateToDetails = { topic, number ->
                    navController.navigate(ScheduleScreenRoute.Details(topic, number))
                }
            )
        }

        // Конфігурація підекрану 2
        composable<ScheduleScreenRoute.Details> { backStackEntry ->
            val details = backStackEntry.toRoute<ScheduleScreenRoute.Details>()

            LectureDetailsSubScreen(
                topicName = details.topicName,
                lectureNumber = details.lectureNumber,
                onNavigateBack = {
                    navController.popBackStack()
                },
                // Перехід з 2-го на 3-й екран
                onNavigateToResources = { topic ->
                    navController.navigate(ScheduleScreenRoute.ExtraResources(topic))
                }
            )
        }

        // Конфігурація підекрану 3
        composable<ScheduleScreenRoute.ExtraResources> { backStackEntry ->
            val resources = backStackEntry.toRoute<ScheduleScreenRoute.ExtraResources>()

            LectureResourcesSubScreen(
                topicName = resources.topicName,
                onNavigateBack = {
                    navController.popBackStack() // Повертає на 2-й екран (Details)
                }
            )
        }
    }
}