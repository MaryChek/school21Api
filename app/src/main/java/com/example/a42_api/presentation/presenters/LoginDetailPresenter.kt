package com.example.a42_api.presentation.presenters

import com.example.a42_api.domain.interactor.UserInteractor
import com.example.a42_api.presentation.contract.LoginDetailContract
import com.example.a42_api.presentation.mapper.LoginDetailMapper
import com.example.a42_api.presentation.models.*
import com.example.a42_api.domain.models.Course as DomainCourse
import com.example.a42_api.domain.models.Coalition as DomainCoalition

class LoginDetailPresenter(
    private val view: LoginDetailContract.View,
    private val interactor: UserInteractor,
    private val mapper: LoginDetailMapper,
) : LoginDetailContract.Presenter {

    private lateinit var user: User
    private var userCourses: List<Course>? = null
    private var currentSelectedCourse = FIRST_COURSE_NUMBER

    override fun init(user: User) {
        this.user = user
        interactor.fetchUserCoursesAndCoalitions(
            user.login,
            this::onGetUserCoursesAndCoalitions,
            this::onServerError
        )
    }

    private fun onGetUserCoursesAndCoalitions(
        courses: List<DomainCourse>,
        coalitions: List<DomainCoalition>
    ) {
        view.showProgressBar(false)

        val userCourses = mapper.mapCourses(courses, coalitions, user.projects)
        val coursesNames: List<String> = userCourses.map { it.name }

        view.showDetail(user, coursesNames)
        updateUserDetail(userCourses[currentSelectedCourse])
        this.userCourses = userCourses
    }

    private fun updateUserDetail(course: Course) {
        updateColorProfile(course.coalition)
        view.updateProgressLevel(course.level)
        updateBackgroundProfile(course.coalition)
        view.updateUserSkills(course.skills)
        updateProjects(course.projects)
        updateCoalition(course.coalition)

    }

    private fun updateColorProfile(coalition: Coalition) =
        if (coalition.colorProfile != null) {
            view.updateColorProfile(coalition.colorProfile)
        } else {
            view.updateColorProfile(coalition.defaultColorResId)
        }

    private fun updateBackgroundProfile(coalition: Coalition) =
        if (coalition.backgroundUrl != null) {
            view.updateBackgroundProfile(coalition.backgroundUrl)
        } else {
            view.updateBackgroundProfile(coalition.defaultBackgroundResId)
        }

    private fun updateProjects(projects: List<Project>) =
        if (projects.isEmpty()) {
            view.showEmptyListProjectsMessage()
        } else {
            view.updateUserProjects(projects)
        }

    private fun updateCoalition(coalition: Coalition) =
        if (coalition.imageUrl != null && coalition.colorProfile != null) {
            view.showUserCoalition(true)
            view.updateUserCoalition(coalition.imageUrl, coalition.colorProfile)
        } else {
            view.showUserCoalition(false)
        }

    private fun onServerError(errorCode: Int?) =
        view.showConnectionErrorLayout(true)

    override fun onCourseClick(name: String) {
        userCourses?.let { userCourses ->
            val courseClicked = userCourses.first { it.name == name }
            val positionCourse: Int = userCourses.indexOf(courseClicked)
            if (positionCourse != currentSelectedCourse) {
                updateUserDetail(courseClicked)
                currentSelectedCourse = positionCourse
            }
        }
    }

    override fun onRetryButtonClick() {
        view.showConnectionErrorLayout(false)
        init(user)
    }

    companion object {
        private const val FIRST_COURSE_NUMBER = 0
    }
}