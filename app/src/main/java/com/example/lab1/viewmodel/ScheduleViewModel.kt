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

import com.example.lab1.util.toDomain

class ScheduleViewModel(
    private val repository: AppRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _apiError = MutableStateFlow<String?>(null)
    val apiError: StateFlow<String?> = _apiError.asStateFlow()

    fun loadDataFromApi(
        tutorsLimit: Int = 4,
        lecturesPerTutor: Int = 2
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _apiError.value = null
            try {
                val remoteTutors = repository.fetchTutorsFromApi().take(tutorsLimit)
                val remoteLectures = repository.fetchLecturesFromApi()

                if (remoteTutors.isEmpty() || remoteLectures.isEmpty()) {
                    _apiError.value = "Зовнішній API повернув порожню відповідь"
                } else {
                    // Очищаємо попередній вміст БД перед збереженням даних із API
                    repository.clearAllData()

                    val domainTutors = remoteTutors.map { it.toDomain() }
                    val insertedLocalIds = repository.addTutorsToDb(domainTutors)

                    val remoteToLocalTutorMap = remoteTutors
                        .zip(insertedLocalIds)
                        .associate { (dto, localId) ->
                            dto.id to dto.toDomain().copy(id = localId.toInt())
                        }

                    val lecturesToSave = remoteTutors.flatMap { tutorDto ->
                        val localTutor = remoteToLocalTutorMap.getValue(tutorDto.id)
                        remoteLectures
                            .filter { it.tutorRemoteId == tutorDto.id }
                            .take(lecturesPerTutor)
                            .map { lectureDto -> lectureDto.toDomain(tutor = localTutor) }
                    }

                    repository.addLecturesToDb(lecturesToSave)
                    loadData()
                }
            } catch (e: Exception) {
                _apiError.value = "Помилка мережі: ${e.localizedMessage ?: "перевірте інтернет"}"
            } finally {
                _isLoading.value = false
            }
        }
    }

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
            repository.clearAllData()
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