package com.capstone.coursemate.dev.data.response.favourites

data class FavoriteRequest(
    val title: String,
    val short_intro: String,
    val url: String,
    val predicted_category: String
)