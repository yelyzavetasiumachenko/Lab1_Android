package com.example.lab1.dto

import com.google.gson.annotations.SerializedName

data class LectureDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("tutorId")
    val tutorRemoteId: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("durationMin")
    val durationMin: Int
)