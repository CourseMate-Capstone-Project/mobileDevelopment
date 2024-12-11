package com.capstone.coursemate.dev.data.response.authentications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


class ResetPasswordViewModel(private val repository: ResetPasswordRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _apiResponse = MutableLiveData<String?>()
    val apiResponse: LiveData<String?> = _apiResponse

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun requestPasswordReset(email: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val result = repository.requestPasswordReset(email)
                if (result.isSuccess) {
                    _apiResponse.value = "Password reset request sent successfully!"
                } else {
                    _error.value = result.exceptionOrNull()?.message
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun resetPassword(email: String, otp: String, newPassword: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val result = repository.resetPassword(email, otp, newPassword)
                if (result.isSuccess) {
                    _apiResponse.value = "Password reset successful!"
                } else {
                    _error.value = result.exceptionOrNull()?.message
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}