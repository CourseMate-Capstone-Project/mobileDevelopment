package com.capstone.coursemate.dev.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.capstone.coursemate.dev.data.response.recommendationAPI.Course

@Dao
interface CourseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(courses: List<Course>)

    @Query("DELETE FROM course_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM course_table ORDER BY id ASC")
    fun getAllCourses(): LiveData<List<Course>>
}