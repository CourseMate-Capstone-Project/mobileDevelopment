package com.capstone.coursemate.dev.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.coursemate.dev.data.response.favourites.FavoritesRepository
// Not being used at the moment
// Preserved
/*class HomeViewModelFactory(
    private val favoritesRepository: FavoritesRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(favoritesRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}*/
