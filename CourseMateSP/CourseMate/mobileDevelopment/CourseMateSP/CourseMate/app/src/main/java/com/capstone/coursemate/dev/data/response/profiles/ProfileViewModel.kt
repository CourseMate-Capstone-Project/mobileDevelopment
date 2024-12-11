package com.capstone.coursemate.dev.data.response.profiles

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: ProfileRepository) : ViewModel() {

    private val _profileData = MutableLiveData<ProfileResponse>()
    val profileData: LiveData<ProfileResponse> = _profileData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _updateResult = MutableLiveData<Result<ProfileResponse>>()
    val updateResult: LiveData<Result<ProfileResponse>> get() = _updateResult
/*
    private val _updateResult = MutableLiveData<Result<String>>()
    val updateResult: LiveData<Result<String>> = _updateResult
*/

    fun fetchProfile(token: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.getProfile(token)
            result.onSuccess { profile ->
                _profileData.value = profile
                Log.e("ProfileViewModel", "Profile Data: $profile")
            }
            result.onFailure { exception ->
                _error.value = exception.message
                Log.d("ProfileViewModel", "Error Fetching Profile: ${exception.message}")
            }
            _isLoading.value = false
        }
    }


    fun updateProfile(token: String, username: String, imageUri: Uri, context: Context) {
        viewModelScope.launch {
            val result = repository.updateProfile(token, username, imageUri, context)
            _updateResult.value = result
        }
    }
/*    fun updateProfile(token: String, request: UpdateProfileRequest) {
        viewModelScope.launch {
            val result = repository.updateProfile(token, request)
            _updateResult.value = result
        }
    }*/

}