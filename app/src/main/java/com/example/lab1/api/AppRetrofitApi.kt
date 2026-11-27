package com.example.lab1.api

import com.example.lab1.dto.LectureDto
import com.example.lab1.dto.TutorDto
import retrofit2.Response
import retrofit2.http.GET

interface AppRetrofitApi {
    @GET("tutors")
    suspend fun getTutors(): Response<List<TutorDto>>

    @GET("lectures")
    suspend fun getLectures(): Response<List<LectureDto>>
}