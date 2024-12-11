package com.capstone.coursemate.dev.data.response.recommendationAPI

data class RecommendationRequest(
    val interest: String,
    val course_type: String,
    val duration: String
)