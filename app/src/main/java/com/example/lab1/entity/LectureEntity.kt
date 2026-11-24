package com.example.lab1.entity

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.ForeignKey
import androidx.room3.PrimaryKey

@Entity(
    tableName = "lectures",
    foreignKeys = [
        ForeignKey(
            entity = TutorEntity::class,
            parentColumns = arrayOf("tutor_id"),
            childColumns = arrayOf("tutor_id"),
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class LectureEntity(
    @ColumnInfo(name = "lecture_title")
    val title: String,

    @ColumnInfo(name = "duration_min")
    val durationMin: Int,

    @ColumnInfo(name = "tutor_id", index = true)
    val tutorId: Int?,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "lecture_id")
    val id: Int = 0
)
