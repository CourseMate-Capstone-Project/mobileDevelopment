@file:Suppress("UNCHECKED_CAST")

package com.capstone.coursemate.dev.data.response.authentications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ResetPasswordViewModelFactory(private val repository: ResetPasswordRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ResetPasswordViewModel::class.java)) {
            return ResetPasswordViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}