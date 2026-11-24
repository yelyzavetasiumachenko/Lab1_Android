package com.example.lab1.repository

import com.example.lab1.model.Lecture
import com.example.lab1.model.MyListable
import com.example.lab1.model.Tutor

interface AppRepository {
    suspend fun getAllData(): List<MyListable>
    suspend fun getAllTutors(): List<Tutor>

    // CRUD для викладачів
    suspend fun addTutor(firstName: String, lastName: String, role: String)
    suspend fun updateTutor(tutor: Tutor)
    suspend fun deleteTutor(tutor: Tutor)

    // CRUD для лекцій
    suspend fun addLecture(title: String, durationMin: Int, tutorId: Int?)
    suspend fun updateLecture(lecture: Lecture)
    suspend fun deleteLecture(lecture: Lecture)

    // Додаткові методи
    suspend fun clearAllData()
    suspend fun restoreDefaultData()
}