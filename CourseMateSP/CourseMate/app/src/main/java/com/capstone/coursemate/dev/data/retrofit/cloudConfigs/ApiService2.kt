package com.capstone.coursemate.dev.data.retrofit.cloudConfigs

import com.capstone.coursemate.dev.data.response.authentications.ApiResponse
import com.capstone.coursemate.dev.data.response.authentications.LoginRequest
import com.capstone.coursemate.dev.data.response.authentications.LoginResponse
import com.capstone.coursemate.dev.data.response.authentications.RegisterRequest
import com.capstone.coursemate.dev.data.response.authentications.ResetPasswordEmailRequest
import com.capstone.coursemate.dev.data.response.authentications.ResetPasswordRequest
import com.capstone.coursemate.dev.data.response.recommendationAPI.Course
import com.capstone.coursemate.dev.data.response.favourites.FavoriteRequest
import com.capstone.coursemate.dev.data.response.profiles.ProfileResponse
import com.capstone.coursemate.dev.data.response.profiles.UpdateProfileRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part

interface ApiService2 {
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<ApiResponse>

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

/*    @POST("auth/forgot-password")
    suspend fun requestPasswordReset(
        @Body request: ResetPasswordEmailRequest
    ): Response<ApiResponse>

    @POST("auth/reset-password")
    suspend fun resetPassword(
        @Body request: ResetPasswordRequest
    ): Response<ApiResponse>*/

    @POST("auth/forgot-password")
    suspend fun requestPasswordReset(@Body email: Map<String, String>): Response<ApiResponse>

    @POST("auth/reset-password")
    suspend fun resetPassword(@Body resetDetails: Map<String, String>): Response<ApiResponse>



    @GET("profile")
    suspend fun getProfile(@Header("Authorization") token: String): Response<ProfileResponse>

    @Multipart
    @PUT("profile")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Part("username") username: RequestBody,
        @Part profilePicture: MultipartBody.Part
    ): Response<ProfileResponse>



    @POST("favorites/toggle")
    suspend fun toggleFavorite(
        @Header("Authorization") token: String,
        @Body favoriteRequest: FavoriteRequest
    ): Response<Unit>

    @GET("favorites")
    suspend fun getFavorites(
        @Header("Authorization") token: String
    ): Response<List<Course>>
}