package com.example.lab1.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AppRetrofitClient {
    private const val BASE_URL = "https://6b0923866a4ecd58b006f2af.mockapi.io/api/v1/"

    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}