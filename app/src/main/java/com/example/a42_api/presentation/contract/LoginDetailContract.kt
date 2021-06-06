package com.example.a42_api.presentation.contract

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.example.a42_api.presentation.models.*

interface LoginDetailContract {
    interface View : BaseContract.View {
        fun showDetail(user: User, coursesNames: List<String>)

        fun updateColorProfile(color: Long)

        fun updateColorProfile(@ColorRes colorResId: Int)

        fun updateProgressLevel(level: Course.Level)

        fun updateBackgroundProfile(imageUrl: String)

        fun updateBackgroundProfile(@DrawableRes drawableResId: Int)

        fun updateUserSkills(skills: List<Skill>)

        fun updateUserProjects(projects: List<Project>)

        fun updateUserCoalition(coalitionImageUrl: String, coalitionBackgroundColor: Long)

        fun showUserCoalition(show: Boolean)

        fun showEmptyListProjectsMessage()
    }

    interface Presenter : BaseContract.Presenter {
        fun init(user: User)

        fun onCourseClick(name: String)
    }
}