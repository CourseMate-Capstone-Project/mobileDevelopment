package com.capstone.coursemate.dev.ui.favorites


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.coursemate.dev.data.database.CourseRepository
import com.capstone.coursemate.dev.data.response.favourites.FavoritesRepository
import com.capstone.coursemate.dev.data.response.recommendationAPI.Course
import kotlinx.coroutines.launch

class FavoritesViewModel(private val favoritesRepository: FavoritesRepository, private val repository: CourseRepository) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is favorites Fragment"
    }
    val text: LiveData<String> = _text

    private val _favorites = MutableLiveData<List<Course>>()
    val favorites: LiveData<List<Course>> get() = _favorites

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun fetchFavorites(token: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val favoriteCourses = favoritesRepository.getFavorites(token)
                _favorites.postValue(favoriteCourses)
                _error.postValue(null)
            } catch (e: Exception) {
                _error.postValue(e.message)
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    //val favoriteCourses: LiveData<List<Course>> = repository.getFavoriteCourses()
    val allFavorites: LiveData<List<Course>> = repository.allCourses
    fun addToFavorites(course: Course) {
        viewModelScope.launch {
            repository.insertCourse(course)
        }
    }

    fun removeFromFavorites(course: Course) {
        viewModelScope.launch {
            repository.deleteCourse(course)
        }
    }
    fun toggleFavorite(course: Course) {
        viewModelScope.launch {
            if (repository.isFavorite(course.id)) {
                repository.deleteCourse(course) // Remove if it exists
            } else {
                repository.insertCourse(course) // Add if it doesn't exist
            }
        }
    }

}