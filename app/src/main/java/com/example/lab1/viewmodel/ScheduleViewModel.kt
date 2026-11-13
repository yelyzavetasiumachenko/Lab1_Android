package com.example.lab1.viewmodel

import androidx.lifecycle.ViewModel
import com.example.lab1.model.MyListable
import com.example.lab1.repository.AppRepository
import com.example.lab1.repository.AppRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ScheduleViewModel : ViewModel() {
    private val repository: AppRepository = AppRepositoryImpl()

    private val _listData = MutableStateFlow<List<MyListable>>(emptyList())
    val listData: StateFlow<List<MyListable>> = _listData.asStateFlow()

    init {
        _listData.value = repository.getAllData()
    }
}