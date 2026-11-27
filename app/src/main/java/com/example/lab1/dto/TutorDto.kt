package com.example.lab1.dto

import com.google.gson.annotations.SerializedName

data class TutorDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("firstName")
    val firstName: String,

    @SerializedName("lastName")
    val lastName: String,

    @SerializedName("role")
    val role: String
)