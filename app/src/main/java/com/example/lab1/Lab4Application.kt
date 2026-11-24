package com.example.lab1

import android.app.Application
import com.example.lab1.db.AppDataBase
import com.example.lab1.repository.AppRepositoryImpl

class Lab4Application : Application() {
    private val appDataBase by lazy { AppDataBase.getDatabase(this) }

    val appRepository by lazy {
        AppRepositoryImpl(appDao = appDataBase.appDao())
    }
}