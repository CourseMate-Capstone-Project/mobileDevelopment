package com.capstone.coursemate.dev.data.retrofit.recommConfigs

import com.capstone.coursemate.dev.data.response.recommendationAPI.RecommendationRequest
import com.capstone.coursemate.dev.data.response.recommendationAPI.RecommendationResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RecommendationApiService {
  @POST("api/recommend")
  fun getRecommendations(@Body request: RecommendationRequest): Call<RecommendationResponse>

}