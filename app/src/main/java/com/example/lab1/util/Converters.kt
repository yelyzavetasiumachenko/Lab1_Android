package com.example.lab1.util

import com.example.lab1.entity.LectureEntity
import com.example.lab1.entity.LectureWithTutor
import com.example.lab1.entity.TutorEntity
import com.example.lab1.model.Lecture
import com.example.lab1.model.Tutor

fun Tutor.toEntity() = TutorEntity(
    firstName = this.firstName,
    lastName = this.lastName,
    role = this.role,
    id = this.id
)

fun Lecture.toEntity() = LectureEntity(
    title = this.title,
    durationMin = this.durationMin,
    tutorId = this.tutor?.id,
    id = this.id
)

fun TutorEntity.toDomain() = Tutor(
    firstName = this.firstName,
    lastName = this.lastName,
    role = this.role,
    id = this.id
)

fun LectureWithTutor.toDomain() = Lecture(
    title = this.lectureEntity.title,
    durationMin = this.lectureEntity.durationMin,
    tutor = this.tutorEntity?.toDomain(),
    id = this.lectureEntity.id
)