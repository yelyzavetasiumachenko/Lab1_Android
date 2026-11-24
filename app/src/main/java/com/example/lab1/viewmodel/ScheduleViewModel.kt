package com.example.lab1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.lab1.Lab4Application
import com.example.lab1.model.Lecture
import com.example.lab1.model.MyListable
import com.example.lab1.model.Tutor
import com.example.lab1.repository.AppRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ScheduleViewModel(
    private val repository: AppRepository
) : ViewModel() {

    private val _listData = MutableStateFlow<List<MyListable>>(emptyList())
    val listData: StateFlow<List<MyListable>> = _listData.asStateFlow()

    private val _tutors = MutableStateFlow<List<Tutor>>(emptyList())
    val tutors: StateFlow<List<Tutor>> = _tutors.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _listData.value = repository.getAllData()
            _tutors.value = repository.getAllTutors()
        }
    }

    fun addTutor(firstName: String, lastName: String, role: String) {
        viewModelScope.launch {
            repository.addTutor(firstName.trim(), lastName.trim(), role.trim())
            loadData()
        }
    }

    fun updateTutor(tutor: Tutor) {
        viewModelScope.launch {
            repository.updateTutor(tutor)
            loadData()
        }
    }

    fun deleteTutor(tutor: Tutor) {
        viewModelScope.launch {
            repository.deleteTutor(tutor)
            loadData()
        }
    }

    fun addLecture(title: String, durationMin: Int, tutorId: Int?) {
        viewModelScope.launch {
            repository.addLecture(title.trim(), durationMin, tutorId)
            loadData()
        }
    }

    fun updateLecture(lecture: Lecture) {
        viewModelScope.launch {
            repository.updateLecture(lecture)
            loadData()
        }
    }

    fun deleteLecture(lecture: Lecture) {
        viewModelScope.launch {
            repository.deleteLecture(lecture)
            loadData()
        }
    }

    fun clearAllData() {
        viewModelScope.launch {
            repository.clearAllData()
            loadData()
        }
    }

    fun restoreDefaultData() {
        viewModelScope.launch {
            repository.restoreDefaultData()
            loadData()
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val application = checkNotNull(
                    extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                ) as Lab4Application
                return ScheduleViewModel(application.appRepository) as T
            }
        }
    }
}