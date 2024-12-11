package com.capstone.coursemate.dev.data.response.profiles

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import com.capstone.coursemate.dev.data.retrofit.cloudConfigs.ApiService2
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class ProfileRepository(private val apiService: ApiService2) {

    suspend fun getProfile(token: String): Result<ProfileResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getProfile("Bearer $token")
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    val error = response.errorBody()?.string() ?: "Unknown error"
                    Result.failure(Exception(error))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun updateProfile(token: String, username: String, imageUri: Uri, context: Context): Result<ProfileResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val usernameBody = username.toRequestBody("text/plain".toMediaTypeOrNull())
                val imagePart = prepareFilePart("profilePicture", imageUri, context)

                val response = apiService.updateProfile("Bearer $token", usernameBody, imagePart)
                if (response.isSuccessful) {
                    Result.success(response.body()!!)
                } else {
                    val error = response.errorBody()?.string() ?: "Unknown error"
                    Result.failure(Exception(error))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    private fun prepareFilePart(partName: String, fileUri: Uri, context: Context): MultipartBody.Part {
        val contentResolver = context.contentResolver
        val inputStream = contentResolver.openInputStream(fileUri)
            ?: throw IllegalArgumentException("Cannot open input stream for URI: $fileUri")

        // Create a temporary file to upload
        val file = File(context.cacheDir, contentResolver.getFileName(fileUri))
        val outputStream = file.outputStream()
        inputStream.use { input -> outputStream.use { output -> input.copyTo(output) } }

        val requestFile = file.asRequestBody(contentResolver.getType(fileUri)?.toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(partName, file.name, requestFile)
    }

    // Helper to get the file name from a URI
    private fun ContentResolver.getFileName(fileUri: Uri): String {
        var name = "temp_file" // Default fallback name
        val cursor = query(fileUri, null, null, null, null)

        cursor?.use {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (nameIndex >= 0 && it.moveToFirst()) {
                name = it.getString(nameIndex) ?: "temp_file"
            }
        }

        return name
    }








    /*private fun prepareFilePart(partName: String, fileUri: Uri, context: Context): MultipartBody.Part {
        val contentResolver = context.contentResolver
        val file = File(fileUri.path ?: "")
        val requestFile = file.asRequestBody(contentResolver.getType(fileUri)?.toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(partName, file.name, requestFile)
    }*/





    /*suspend fun updateProfile(token: String, request: UpdateProfileRequest): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                Log.e("ProfileRepository", "Token: $token, Request: $request")
                val response = apiService.updateProfile("Bearer $token", request)
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()?.message ?: "Profile updated successfully")
                } else {
                    val error = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("ProfileRepository", "API Error: $error")
                    Result.failure(Exception(error))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }*/
}