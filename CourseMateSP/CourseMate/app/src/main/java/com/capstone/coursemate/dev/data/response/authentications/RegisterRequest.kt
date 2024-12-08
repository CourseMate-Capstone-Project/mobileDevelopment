package com.capstone.coursemate.dev.data.response.authentications

data class RegisterRequest(
    val username: String,
    val fullName: String,
    val email: String,
    val password: String
)
