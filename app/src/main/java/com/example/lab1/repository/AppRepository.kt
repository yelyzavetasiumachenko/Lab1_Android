package com.example.lab1.repository

import com.example.lab1.model.MyListable

interface AppRepository {
    fun getAllData(): List<MyListable>
}