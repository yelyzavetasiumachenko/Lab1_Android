package com.example.lab1.model

data class Lecture(
    val title: String,
    val durationMin: Int,
    val tutor: Tutor?,
    override val id: Int
) : MyListable