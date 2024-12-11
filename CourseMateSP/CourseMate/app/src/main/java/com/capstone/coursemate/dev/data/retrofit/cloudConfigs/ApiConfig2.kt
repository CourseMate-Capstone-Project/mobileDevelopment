package com.capstone.coursemate.dev.data.retrofit.cloudConfigs

import cz.msebera.httpclient.android.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/*object ApiConfig2 {
    private const val BASE_URL = "https://capstone-project-442014.et.r.appspot.com/" // Your base API URL

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ApiService2 = retrofit.create(ApiService2::class.java)
}*/
object ApiConfig2 {
    private const val BASE_URL = "https://capstone-project-442014.et.r.appspot.com/api/" //     https://f7b3-125-162-30-210.ngrok-free.app/api/

    // Create OkHttpClient with logging interceptor
    private val client: OkHttpClient by lazy {
        val loggingInterceptor = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        } else {
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.NONE
            }
        }

        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS) // Set connection timeout
            .readTimeout(30, TimeUnit.SECONDS)    // Set read timeout
            .writeTimeout(30, TimeUnit.SECONDS)   // Set write timeout
            .build()
    }

    // Retrofit instance
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client) // Attach OkHttpClient
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // ApiService instance
    val apiService: ApiService2 by lazy {
        retrofit.create(ApiService2::class.java)
    }
}

/*

class ApiConfig2 {
    companion object{
        fun getApiService(): ApiService2 {
            val loggingInterceptor = if(BuildConfig.DEBUG) {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            } else {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
            }
            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl("https://capstone-project-442014.et.r.appspot.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ApiService2::class.java)
        }
    }
}*/
