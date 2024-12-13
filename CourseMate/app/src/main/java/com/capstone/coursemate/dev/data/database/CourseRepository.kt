package com.capstone.coursemate.dev.data.database

import androidx.lifecycle.LiveData
import com.capstone.coursemate.dev.data.response.recommendationAPI.Course

class CourseRepository(private val courseDao: CourseDao) {


    suspend fun insertCourse(course: Course) {
        courseDao.insertCourse(course)
    }

    suspend fun deleteCourse(course: Course) {
        courseDao.deleteCourse(course)
    }
    suspend fun isFavorite(courseId: Int): Boolean {
        return courseDao.getCourseById(courseId) != null
    }

    fun getFavoriteCourses(): LiveData<List<Course>> {
        return courseDao.getFavoriteCourses()
    }






    val allCourses: LiveData<List<Course>> = courseDao.getAllCourses()

    suspend fun insertAll(courses: List<Course>) {
        courseDao.insertAll(courses)
    }

    suspend fun deleteAll() {
        courseDao.deleteAll()
    }


}