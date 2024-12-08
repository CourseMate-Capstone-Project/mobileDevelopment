package com.capstone.coursemate.dev.data.response.authentications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _authResult = MutableLiveData<Result<String>>()
    val authResult: LiveData<Result<String>> = _authResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun register(username: String, fullName: String, email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.register(username, fullName, email, password)
            _authResult.value = result
            _isLoading.value = false
        }
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.login(username, password)
            _authResult.value = result
            _isLoading.value = false
        }
    }
}