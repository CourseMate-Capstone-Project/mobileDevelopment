package com.capstone.coursemate.dev.data.response.authentications

data class ResetPasswordRequest(
    val email: String,
    val otp: String,
    val newPassword: String
)
