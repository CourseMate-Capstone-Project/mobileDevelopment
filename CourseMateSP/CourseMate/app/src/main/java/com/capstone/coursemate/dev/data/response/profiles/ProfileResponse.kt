package com.capstone.coursemate.dev.data.response.profiles


data class ProfileResponse(
    val id: Int,
    val username: String,
    val full_name: String,
    val email: String,
    val profile_picture: String
)
