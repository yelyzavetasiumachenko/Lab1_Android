package com.example.lab1.model

data class Tutor(
    val firstName: String,
    val lastName: String,
    val role: String,
    override val id: Int
) : MyListable {
    val fullName: String
        get() = "$firstName $lastName"
}