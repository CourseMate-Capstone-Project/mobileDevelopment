package com.capstone.coursemate.dev.data.retrofit.recommConfigs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.capstone.coursemate.dev.data.response.recommendationAPI.RecommendationRequest
import com.capstone.coursemate.dev.data.response.recommendationAPI.RecommendationResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RecommendationRepository {

    private val apiService: RecommendationApiService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://course-recommendation-api-866939629489.asia-southeast2.run.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(RecommendationApiService::class.java)
    }

    fun getRecommendations(request: RecommendationRequest): LiveData<Result<RecommendationResponse>> {
        val liveData = MutableLiveData<Result<RecommendationResponse>>()
        apiService.getRecommendations(request).enqueue(object : retrofit2.Callback<RecommendationResponse> {
            override fun onResponse(
                call: retrofit2.Call<RecommendationResponse>,
                response: retrofit2.Response<RecommendationResponse>
            ) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        liveData.postValue(Result.success(body))
                    } else {
                        liveData.postValue(Result.failure(Throwable("Response body is null.")))
                    }
                } else {
                    liveData.postValue(Result.failure(Throwable("Failed to load recommendations. Status code: ${response.code()}")))
                }
            }

            override fun onFailure(call: retrofit2.Call<RecommendationResponse>, t: Throwable) {
                liveData.postValue(Result.failure(t))
            }
        })
        return liveData
    }
}