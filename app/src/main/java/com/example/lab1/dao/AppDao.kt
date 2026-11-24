package com.example.lab1.dao

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import androidx.room3.Transaction
import androidx.room3.Update
import com.example.lab1.entity.LectureEntity
import com.example.lab1.entity.LectureWithTutor
import com.example.lab1.entity.TutorEntity

@Dao
interface AppDao {
    // --- Tutor DAO ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTutor(tutorEntity: TutorEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllTutors(tutorsEntity: List<TutorEntity>): List<Long>

    @Update
    suspend fun updateTutor(tutorEntity: TutorEntity)

    @Delete
    suspend fun delTutor(tutorEntity: TutorEntity)

    @Query("DELETE FROM tutors")
    suspend fun delAllTutors()

    @Query("SELECT * FROM tutors ORDER BY tutor_id ASC")
    suspend fun getTutors(): List<TutorEntity>

    // --- Lecture DAO ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLecture(lectureEntity: LectureEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllLectures(lecturesEntity: List<LectureEntity>)

    @Update
    suspend fun updateLecture(lectureEntity: LectureEntity)

    @Delete
    suspend fun delLecture(lectureEntity: LectureEntity)

    @Query("DELETE FROM lectures")
    suspend fun delAllLectures()

    @Transaction
    @Query("SELECT * FROM lectures ORDER BY lecture_id ASC")
    suspend fun getLectures(): List<LectureWithTutor>
}