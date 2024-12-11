package com.capstone.coursemate.dev.data.response.recommendationAPI

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "course_table")
data class Course(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,  // This attribute will serve as a primary key for Room.
    val title: String ="",
    val short_intro: String ="",
    val url: String ="",

    val category: String ="", // Common
    val interest: String ="", // Common
    var isFavorite: Boolean = false // Used for favorites

)