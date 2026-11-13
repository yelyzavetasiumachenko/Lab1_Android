package com.example.lab1.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class GradesViewModel : ViewModel() {
    var score by mutableIntStateOf(45)
        private set

    fun changeScore(amount: Int) {
        score += amount
        if (score < 0) score = 0
        if (score > 100) score = 100
    }
}