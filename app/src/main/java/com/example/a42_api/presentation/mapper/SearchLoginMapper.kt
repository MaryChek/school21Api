package com.example.a42_api.presentation.mapper

import com.example.a42_api.BaseMapper
import com.example.a42_api.presentation.models.*
import com.example.a42_api.domain.models.Project as DomainProject
import com.example.a42_api.domain.models.User as DomainUser

class SearchLoginMapper: BaseMapper() {
    fun mapUser(user: DomainUser) =
        User(
            user.email,
            user.login,
            user.fullName,
            user.imageUrl,
            user.location ?: LOCATION_UNAVAILABLE,
            user.points,
            user.wallet,
            mapProjects(user.projects),
            user.campuses.firstOrNull() { it.isActive }?.city ?: NO_CITY
        )

    private fun mapProjects(projects: List<DomainProject>): List<Project> =
        projects.map {
            Project(
                it.id,
                it.parentId,
                it.courseIds,
                firstUpperCase(it.name),
                it.finalMark,
                it.status,
                it.isValidated
            )
        }

    companion object {
        private const val LOCATION_UNAVAILABLE = "Unavailable"
        private const val NO_CITY = "No City"
    }
}