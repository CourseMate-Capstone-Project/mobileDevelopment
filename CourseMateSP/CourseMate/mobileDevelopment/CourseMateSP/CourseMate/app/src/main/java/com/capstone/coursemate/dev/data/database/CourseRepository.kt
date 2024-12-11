package com.capstone.coursemate.dev.data.database

import androidx.lifecycle.LiveData
import com.capstone.coursemate.dev.data.response.recommendationAPI.Course
import com.capstone.coursemate.dev.data.response.recommendationAPI.RecommendationRequest
import com.capstone.coursemate.dev.data.response.recommendationAPI.RecommendationResponse
import com.capstone.coursemate.dev.data.retrofit.recommConfigs.RecommendationApiService
import com.capstone.coursemate.dev.data.retrofit.recommConfigs.RecommendationRepository

class CourseRepository(private val courseDao: CourseDao) {

    val allCourses: LiveData<List<Course>> = courseDao.getAllCourses()

    suspend fun insertAll(courses: List<Course>) {
        courseDao.insertAll(courses)
    }

    suspend fun deleteAll() {
        courseDao.deleteAll()
    }
/*    // Function to make network call to fetch recommendations
    suspend fun fetchRecommendations(request: RecommendationRequest): Result<RecommendationResponse> {
        // Implement your API call logic here
        // For example, if you are using Retrofit:
        return try {
            val response = RecommendationApiService
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("API call failed with response code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }*/
}