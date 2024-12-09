package com.capstone.coursemate.dev.data.response.favourites

import com.capstone.coursemate.dev.data.response.recommendationAPI.Course
import com.capstone.coursemate.dev.data.retrofit.cloudConfigs.ApiService2

class FavoritesRepository(private val apiService: ApiService2) {
    suspend fun toggleFavorite(token: String, favoriteRequest: FavoriteRequest): Result<Unit> {
        return try {
            val response = apiService.toggleFavorite("Bearer $token", favoriteRequest)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed with code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }/*
    class FavoritesRepository(private val apiService: ApiService2) {

        suspend fun toggleFavorite(token: String, request: FavoriteRequest): Result<Unit> {
            return try {
                val response = apiService.toggleFavorite("Bearer $token", request)
                if (response.isSuccessful) {
                    Result.success(Unit)
                } else {
                    Result.failure(Exception("API Error: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }*/

    suspend fun getFavorites(token: String): List<Course> {
        val response = apiService.getFavorites("Bearer $token")
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        } else {
            throw Exception(response.errorBody()?.string())
        }
    }
}