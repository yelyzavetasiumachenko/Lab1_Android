package com.example.lab1.entity

import androidx.room3.Embedded
import androidx.room3.Relation

data class LectureWithTutor(
    @Embedded val lectureEntity: LectureEntity,
    @Relation(
        parentColumns = ["tutor_id"],
        entityColumns = ["tutor_id"]
    )
    val tutorEntity: TutorEntity?
)