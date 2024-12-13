package com.capstone.coursemate.dev.data.response.authentications

import android.content.Context
import android.content.SharedPreferences

class TokenManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE)

/*    private val _token = MutableLiveData<String?>()
    val token: LiveData<String?> get() = _token

    init {
        _token.value = sharedPreferences.getString("auth_token", null)

    }*/
    fun saveToken(token: String) {
        sharedPreferences.edit().putString("auth_token", token).apply()
    }

    fun getToken(): String? {
        return sharedPreferences.getString("auth_token", null)
    }

    fun clearToken() {
        sharedPreferences.edit().remove("auth_token").apply()
    }
}