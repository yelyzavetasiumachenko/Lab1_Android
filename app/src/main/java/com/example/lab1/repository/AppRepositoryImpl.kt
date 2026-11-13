package com.example.lab1.repository

import com.example.lab1.model.*

class AppRepositoryImpl : AppRepository {
    private val tutorsList: List<Tutor> = listOf(
        Tutor("Іван Петренко", "Senior JS Developer", id = 1),
        Tutor("Марія Коваленко", "Frontend Tech Lead", id = 2),
        Tutor("Олексій Сидорчук", "Web Designer", id = 3)
    )

    private val courseData: List<MyListable> = listOf(
        HorizontalTutorsList(tutorsList, id = 100),

        HeaderItem(title = "\uD83D\uDCDA Матеріали курсу:", id = 101),

        Tutor("Іван Петренко", "автор курсу", id = 1),
        Lecture("Введення у JavaScript", 45, tutorsList[0], id = 1),
        Lecture("Document Object Model", 55, tutorsList[0], id = 5),

        Tutor("Марія Коваленко", "куратор", id = 2),
        Lecture("Об'єкт. Масиви", 60, tutorsList[1], id = 2),
        Lecture("Опрацювання подій", 40, tutorsList[1], id = 3),

        Tutor("Олексій Сидорчук", "ментор", id = 3),
        Lecture("Browser Object Model", 50, tutorsList[2], id = 4)
    )

    override fun getAllData(): List<MyListable> {
        return courseData
    }
}