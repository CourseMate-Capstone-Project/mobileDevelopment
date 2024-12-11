package com.capstone.coursemate.dev.data.response.authentications

import com.capstone.coursemate.dev.data.retrofit.cloudConfigs.ApiService2

class ResetPasswordRepository(private val apiService: ApiService2) {

    // Request password reset
    suspend fun requestPasswordReset(email: String): Result<ApiResponse> {
        return try {
            val response = apiService.requestPasswordReset(mapOf("email" to email))
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.message()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Confirm password reset
    suspend fun resetPassword(email: String, otp: String, newPassword: String): Result<ApiResponse> {
        return try {
            val response = apiService.resetPassword(
                mapOf(
                    "email" to email,
                    "otp" to otp,
                    "newPassword" to newPassword
                )
            )
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.message()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}