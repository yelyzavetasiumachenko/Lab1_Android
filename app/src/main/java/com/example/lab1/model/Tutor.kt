package com.example.lab1.model

data class Tutor(
    val name: String,
    val role: String,
    override val id: Int
) : MyListable