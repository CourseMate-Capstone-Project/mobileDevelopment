package com.capstone.coursemate.dev.data.response.favourites

data class Course(
    val id: String,
    val title: String,
    val url: String,
    val category: String,
    val interest: String,
    var isFavorite: Boolean = false // Local state for favorite toggle
)