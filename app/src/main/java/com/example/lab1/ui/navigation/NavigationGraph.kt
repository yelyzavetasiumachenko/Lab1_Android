package com.example.lab1.ui.navigation

import kotlinx.serialization.Serializable

sealed interface ScheduleScreenRoute {

    @Serializable
    data object List : ScheduleScreenRoute

    @Serializable
    data class Details(val topicName: String, val lectureNumber: Int) : ScheduleScreenRoute

    @Serializable
    data class ExtraResources(val topicName: String) : ScheduleScreenRoute
}