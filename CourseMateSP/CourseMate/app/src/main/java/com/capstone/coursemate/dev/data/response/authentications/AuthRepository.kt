package com.capstone.coursemate.dev.data.response.authentications

//import com.capstone.coursemate.dev.data.retrofit.cloudConfigs.ApiConfig2
import android.util.Log
import com.capstone.coursemate.dev.data.retrofit.cloudConfigs.ApiService2
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class AuthRepository(private val apiService: ApiService2, private val tokenManager: TokenManager) {

    suspend fun register(username: String, fullName: String, email: String, password: String): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val request = RegisterRequest(username, fullName, email, password)
                val response = apiService.register(request)

                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!.message)
                } else {
                    val error = response.errorBody()?.string() ?: "Unknown error"
                    Result.failure(Exception(error))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun login(username: String, password: String): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val request = LoginRequest(username, password)
                val response = apiService.login(request)

                if (response.isSuccessful && response.body() != null) {
                    val token = response.body()!!.token
                    Log.e("AuthRepsitory", "TOKEN FRROM SERVER IS: $token")
                    tokenManager.saveToken(token)
                    Result.success("Login successful")
                } else {
                    val error = response.errorBody()?.string() ?: "Unknown error"
                    Result.failure(Exception(error))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

}