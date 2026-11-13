package com.example.lab1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lab1.ui.theme.Lab1Theme
import com.example.lab1.viewmodel.GradesViewModel

import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute

import com.example.lab1.ui.navigation.*
import com.example.lab1.ui.screens.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab1Theme {
                Lab1App()
            }
        }
    }
}

enum class AppDestinations(
    val label: String,
    val icon: Int,
) {
    SCHEDULE("Schedule", R.drawable.ic_schedule),
    GRADES("Grades", R.drawable.ic_grade),
}


@PreviewScreenSizes
@Composable
fun Lab1App() {
    val navController = rememberNavController()


    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach { appDest ->

                val isSelected = when (appDest) {
                    AppDestinations.SCHEDULE -> currentDestination?.hierarchy?.any { it.hasRoute(ScheduleTabRoute::class) } == true
                    AppDestinations.GRADES -> currentDestination?.hierarchy?.any { it.hasRoute(GradesTabRoute::class) } == true
                }

                item(
                    icon = { Icon(painterResource(appDest.icon), contentDescription = appDest.label) },
                    label = { Text(appDest.label) },
                    selected = isSelected,
                    onClick = {
                        val route = if (appDest == AppDestinations.SCHEDULE) ScheduleTabRoute else GradesTabRoute

                        navController.navigate(route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = ScheduleTabRoute,
            modifier = Modifier.fillMaxSize()
        ) {
            navigation<ScheduleTabRoute>(startDestination = ScheduleScreenRoute.List) {
                composable<ScheduleScreenRoute.List> {
                    ScheduleListSubScreen(
                        onNavigateToDetails = { topic, number ->
                            navController.navigate(ScheduleScreenRoute.Details(topic, number))
                        }
                    )
                }

                composable<ScheduleScreenRoute.Details> { backStackEntry ->
                    val details = backStackEntry.toRoute<ScheduleScreenRoute.Details>()
                    LectureDetailsSubScreen(
                        topicName = details.topicName,
                        lectureNumber = details.lectureNumber,
                        onNavigateBack = { navController.popBackStack() },
                        onNavigateToResources = { topic ->
                            navController.navigate(ScheduleScreenRoute.ExtraResources(topic))
                        }
                    )
                }

                composable<ScheduleScreenRoute.ExtraResources> { backStackEntry ->
                    val resources = backStackEntry.toRoute<ScheduleScreenRoute.ExtraResources>()
                    LectureResourcesSubScreen(
                        topicName = resources.topicName,
                        onNavigateBack = { navController.popBackStack() }
                    )
                }
            }

            composable<GradesTabRoute> {
                GradesScreen()
            }
        }
    }
}

@Composable
fun GradesScreen(
    modifier: Modifier = Modifier,
    viewModel: GradesViewModel = viewModel()
) {
    val isPassed = viewModel.score >= 60
    val statusText = if (isPassed) "Курс зараховано!" else "Потрібно набрати від 60 балів"
    val statusColor = if (isPassed) androidx.compose.ui.graphics.Color.Green else androidx.compose.ui.graphics.Color.Red

    Box(
        modifier = modifier.fillMaxSize().padding(16.dp)
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Рейтинг студента: ${viewModel.score}",
                style = androidx.compose.material3.MaterialTheme.typography.headlineMedium
            )
            Text(
                text = statusText,
                color = statusColor,
                style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Button(
            onClick = { viewModel.changeScore(-5) },
            modifier = Modifier.align(Alignment.BottomStart),
            enabled = viewModel.score > 0
        ) {
            Text("-5 балів")
        }

        Button(
            onClick = { viewModel.changeScore(5) },
            modifier = Modifier.align(Alignment.BottomEnd),
            enabled = viewModel.score < 100
        ) {
            Text("+5 балів")
        }
    }
}


//@Composable
//fun ScheduleScreen(modifier: Modifier = Modifier) {
//    val topics = listOf("Введення у JavaScript", "Об'єкт. Масиви. Об'єкт Array", "Опрацювання подій", "Browser Object Model", "Document Object Model")
//
//    var currentIndex by rememberSaveable { mutableIntStateOf(0) }
//
//    Column(
//        modifier = modifier.fillMaxSize().padding(16.dp),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text(
//            text = "Курс: Основи мови програмування JavaScript",
//            style = androidx.compose.material3.MaterialTheme.typography.titleLarge,
//            modifier = Modifier.padding(bottom = 24.dp),
//            textAlign = androidx.compose.ui.text.style.TextAlign.Center
//        )
//
//        Text(
//            text = "Лекція ${currentIndex + 1}: ${topics[currentIndex]}",
//            style = androidx.compose.material3.MaterialTheme.typography.bodyLarge,
//            modifier = Modifier.padding(bottom = 32.dp)
//        )
//
//        Row(
//            horizontalArrangement = Arrangement.spacedBy(16.dp)
//        ) {
//            Button(
//                onClick = { currentIndex-- },
//                enabled = currentIndex > 0
//            ) {
//                Text("Попередня")
//            }
//
//            Button(
//                onClick = { currentIndex++ },
//                enabled = currentIndex < topics.size - 1
//            ) {
//                Text("Наступна")
//            }
//        }
//    }
//}



//
//@Composable
//fun GradesScreen(
//    modifier: Modifier = Modifier
//) {
//    var score by remember { mutableIntStateOf(45) }
//
//    val isPassed = score >= 60
//    val statusText = if (isPassed) "Курс зараховано!" else "Потрібно набрати від 60 балів"
//    val statusColor = if (isPassed) androidx.compose.ui.graphics.Color.Green else androidx.compose.ui.graphics.Color.Red
//
//    Box(
//        modifier = modifier.fillMaxSize().padding(16.dp)
//    ) {
//        Column(
//            modifier = Modifier.align(Alignment.Center),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Text(
//                text = "Рейтинг студента: $score",
//                style = androidx.compose.material3.MaterialTheme.typography.headlineMedium
//            )
//            Text(
//                text = statusText,
//                color = statusColor,
//                style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
//                modifier = Modifier.padding(top = 8.dp)
//            )
//        }
//
//        Button(
//            onClick = {
//                score -= 5
//                if (score < 0) score = 0
//            },
//            modifier = Modifier.align(Alignment.BottomStart),
//            enabled = score > 0
//        ) {
//            Text("-5 балів")
//        }
//
//        Button(
//            onClick = {
//                score += 5
//                if (score > 100) score = 100
//            },
//            modifier = Modifier.align(Alignment.BottomEnd),
//            enabled = score < 100
//        ) {
//            Text("+5 балів")
//        }
//    }
//}


