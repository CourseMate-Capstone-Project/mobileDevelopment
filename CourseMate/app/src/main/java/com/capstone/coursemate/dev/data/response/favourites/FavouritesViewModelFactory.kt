package com.capstone.coursemate.dev.data.response.favourites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.coursemate.dev.data.database.CourseRepository
import com.capstone.coursemate.dev.ui.favorites.FavoritesViewModel

class FavoritesViewModelFactory(
    private val favoritesRepository: FavoritesRepository,
    private val courseRepository: CourseRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoritesViewModel::class.java)) {
            return FavoritesViewModel(favoritesRepository, courseRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}