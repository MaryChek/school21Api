package com.example.a42_api.presentation.models

class UserDetailModel {
    private var userCourses: List<Course>? = null
    private var user: User? = null
    var currentSelectedCourse = FIRST_COURSE_NUMBER

    fun updateUser(user: User) {
        this.user = user
    }

    fun getUser(): User? =
        user

    fun isFindUserByLogin(login: String): Boolean =
        when (user?.login == login) {
            true ->
                true
            false -> {
                removeDate()
                false
            }
        }

    fun updateCourses(courses: List<Course>) {
        this.userCourses = courses
    }

    fun getCourses(): List<Course>? =
        this.userCourses

    fun removeDate() {
        user = null
        userCourses = null
        currentSelectedCourse = FIRST_COURSE_NUMBER
    }

    companion object {
        private const val FIRST_COURSE_NUMBER = 0
    }
}