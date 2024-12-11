package com.capstone.coursemate.dev.data.response.favourites

data class FavoriteRequest(
    val id: String,
    val title: String,
    val url: String,
    val category: String,
    val interest: String
)