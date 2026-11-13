package com.example.lab1.model

data class HorizontalTutorsList(
    val tutors: List<Tutor>,
    override val id: Int
) : MyListable