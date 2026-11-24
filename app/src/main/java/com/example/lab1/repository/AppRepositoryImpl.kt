package com.example.lab1.repository

import com.example.lab1.dao.AppDao
import com.example.lab1.entity.LectureEntity
import com.example.lab1.entity.TutorEntity
import com.example.lab1.model.*
import com.example.lab1.util.toEntity
import com.example.lab1.util.toDomain

class AppRepositoryImpl(
    private val appDao: AppDao
) : AppRepository {

    // Прапорець гарантує, що початкове заповнення спрацює лише 1 раз при запуску
    private var isInitialCheckDone = false

    private suspend fun populateInitialDataIfEmpty() {
        if (isInitialCheckDone) return
        isInitialCheckDone = true

        if (appDao.getTutors().isEmpty() && appDao.getLectures().isEmpty()) {
            insertDefaultRecords()
        }
    }

    private suspend fun insertDefaultRecords() {
        val insertedIds = appDao.insertAllTutors(
            listOf(
                TutorEntity(firstName = "Іван", lastName = "Петренко", role = "Senior JS Developer"),
                TutorEntity(firstName = "Марія", lastName = "Коваленко", role = "Frontend Tech Lead"),
                TutorEntity(firstName = "Олексій", lastName = "Сидорчук", role = "Web Designer")
            )
        )
        if (insertedIds.size >= 3) {
            val ivanId = insertedIds[0].toInt()
            val mariaId = insertedIds[1].toInt()
            val oleksiyId = insertedIds[2].toInt()

            appDao.insertAllLectures(
                listOf(
                    LectureEntity("Введення у JavaScript", 45, ivanId),
                    LectureEntity("Document Object Model", 55, ivanId),
                    LectureEntity("Об'єкт. Масиви. Об'єкт Array", 60, mariaId),
                    LectureEntity("Опрацювання подій", 40, mariaId),
                    LectureEntity("Browser Object Model", 50, oleksiyId)
                )
            )
        }
    }

    override suspend fun getAllData(): List<MyListable> {
        populateInitialDataIfEmpty()

        val tutorEntities = appDao.getTutors()
        val lectureWithTutors = appDao.getLectures()

        val tutors = tutorEntities.map { it.toDomain() }
        val result = mutableListOf<MyListable>()

        if (tutors.isNotEmpty()) {
            result.add(HorizontalTutorsList(tutors, id = 100))
        }

        if (tutors.isNotEmpty() || lectureWithTutors.isNotEmpty()) {
            result.add(HeaderItem(title = "📚 Матеріали курсу:", id = 101))
        }

        // Групуємо викладача та його лекції
        for (tutor in tutors) {
            result.add(tutor)
            val tutorLectures = lectureWithTutors
                .filter { it.lectureEntity.tutorId == tutor.id }
                .map { it.toDomain() }
            result.addAll(tutorLectures)
        }

        // Якщо є лекції без прив'язаного викладача (tutorId == null)
        val unassignedLectures = lectureWithTutors
            .filter { it.lectureEntity.tutorId == null }
            .map { it.toDomain() }
        result.addAll(unassignedLectures)

        return result
    }

    override suspend fun getAllTutors(): List<Tutor> {
        return appDao.getTutors().map { it.toDomain() }
    }

    override suspend fun addTutor(firstName: String, lastName: String, role: String) {
        appDao.insertTutor(
            TutorEntity(firstName = firstName, lastName = lastName, role = role)
        )
    }

    override suspend fun updateTutor(tutor: Tutor) {
        appDao.updateTutor(tutor.toEntity())
    }

    override suspend fun deleteTutor(tutor: Tutor) {
        appDao.delTutor(tutor.toEntity())
    }

    override suspend fun addLecture(title: String, durationMin: Int, tutorId: Int?) {
        appDao.insertLecture(
            LectureEntity(title = title, durationMin = durationMin, tutorId = tutorId)
        )
    }

    override suspend fun updateLecture(lecture: Lecture) {
        appDao.updateLecture(lecture.toEntity())
    }

    override suspend fun deleteLecture(lecture: Lecture) {
        appDao.delLecture(lecture.toEntity())
    }

    override suspend fun clearAllData() {
        appDao.delAllLectures()
        appDao.delAllTutors()
    }

    override suspend fun restoreDefaultData() {
        clearAllData()
        insertDefaultRecords()
    }
}




















//package com.example.lab1.repository
//
//import com.example.lab1.model.*
//
//class AppRepositoryImpl : AppRepository {
//    private val tutorsList: List<Tutor> = listOf(
//        Tutor("Іван Петренко", "Senior JS Developer", id = 1),
//        Tutor("Марія Коваленко", "Frontend Tech Lead", id = 2),
//        Tutor("Олексій Сидорчук", "Web Designer", id = 3)
//    )
//
//    private val courseData: List<MyListable> = listOf(
//
//
//        HeaderItem(title = "\uD83D\uDCDA Матеріали курсу:", id = 101),
//
////        Tutor("Іван Петренко", "автор курсу", id = 1),
//        tutorsList[0].copy(role = "автор курсу"),
//        Lecture("Введення у JavaScript", 45, tutorsList[0], id = 1),
//        Lecture("Document Object Model", 55, tutorsList[0], id = 5),
//
////        Tutor("Марія Коваленко", "куратор", id = 2),
//        tutorsList[1].copy(role = "куратор"),
//        Lecture("Об'єкт. Масиви", 60, tutorsList[1], id = 2),
//        Lecture("Опрацювання подій", 40, tutorsList[1], id = 3),
//        HorizontalTutorsList(tutorsList, id = 100),
//
////        Tutor("Олексій Сидорчук", "ментор", id = 3),
//        tutorsList[2].copy(role = "ментор"),
//        Lecture("Browser Object Model", 50, tutorsList[2], id = 4)
//
//    )
//
//    override fun getAllData(): List<MyListable> {
//        return courseData
//    }
//}