package com.capstone.coursemate.dev.data.response.recommendationAPI

data class RecommendationResponse(
    val predicted_category: String,
    val recommended_courses: List<Course>
)