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
    private val model: UserDetailModel,
    private val mapper: LoginDetailMapper,
) : LoginDetailContract.Presenter {

    override fun init(user: User) {
        if (model.isFindUserByLogin(user.login)) {
            val courses = model.getCourses()
            courses?.let {
                showUserDetail(courses)
            }
        } else {
            model.updateUser(user)
            interactor.fetchUserCoursesAndCoalitions(
                user.login,
                this::onGetUserCoursesAndCoalitions,
                this::onServerError
            )
        }
    }

    private fun onGetUserCoursesAndCoalitions(
        courses: List<DomainCourse>,
        coalitions: List<DomainCoalition>
    ) {
        val projects = model.getUser()?.projects
        projects?.let {
            val userCourses = mapper.mapCourses(courses, coalitions, projects)
            showUserDetail(userCourses)
            model.updateCourses(userCourses)
        }
    }

    private fun showUserDetail(userCourses: List<Course>) {
        view.showProgressBar(false)

        val coursesNames: List<String> = userCourses.map { it.name }
        val user = model.getUser()
        user?.let {
            view.showDetail(user, coursesNames)
            val currentSelectedCourse = model.currentSelectedCourse
            updateUserDetail(userCourses[currentSelectedCourse])
        }
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
        val userCourses = model.getCourses()
        if (userCourses != null) {
            val courseClicked = userCourses.first { it.name == name }
            val positionCourse: Int = userCourses.indexOf(courseClicked)
            if (positionCourse != model.currentSelectedCourse) {
                updateUserDetail(courseClicked)
                model.currentSelectedCourse = positionCourse
            }
        }
    }

    override fun onRetryButtonClick() {
        view.showConnectionErrorLayout(false)
        model.getUser()?.let { user ->
            model.removeDate()
            init(user)
        }
    }
}