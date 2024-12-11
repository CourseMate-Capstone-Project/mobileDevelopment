package com.capstone.coursemate.dev.data.response.recommendationAPI

import com.google.gson.annotations.SerializedName

//GET ENDPOINT
data class GetRecommendationAPI(

	@field:SerializedName("recommended_courses")
	val recommendedCourses: List<RecommendedCoursesItem?>? = null,

	@field:SerializedName("predicted_category")
	val predictedCategory: String? = null
)

data class RecommendedCoursesItem(

	@field:SerializedName("short_intro")
	val shortIntro: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("url")
	val url: String? = null
)



//POST ENDPOINT
data class PostRecommendationAPI(

	@field:SerializedName("interest")
	val interest: String? = null,

	@field:SerializedName("course_type")
	val course_type: String? = null,

	@field:SerializedName("duration")
	val duration: String? = null,

	)