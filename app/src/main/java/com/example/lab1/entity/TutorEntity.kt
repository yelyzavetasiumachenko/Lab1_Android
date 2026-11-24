package com.example.lab1.entity

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "tutors")
data class TutorEntity(
    @ColumnInfo(name = "first_name")
    val firstName: String,

    @ColumnInfo(name = "last_name")
    val lastName: String,

    @ColumnInfo(name = "tutor_role")
    val role: String,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "tutor_id")
    val id: Int = 0
)