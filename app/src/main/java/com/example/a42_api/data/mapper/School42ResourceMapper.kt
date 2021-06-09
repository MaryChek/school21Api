package com.example.a42_api.data.mapper

import com.example.a42_api.data.api.models.*
import com.example.a42_api.domain.models.Skill as DomainSkill
import com.example.a42_api.domain.models.Campus as DomainCampus
import com.example.a42_api.domain.models.Course as DomainCourse
import com.example.a42_api.domain.models.Project as DomainProject
import com.example.a42_api.domain.models.Token as DomainToken
import com.example.a42_api.domain.models.User as DomainUser
import com.example.a42_api.domain.models.CoalitionId as DomainCoalitionId
import com.example.a42_api.domain.models.Coalition as DomainCoalition

class School42ResourceMapper {
    fun mapToken(token: Token) =
        DomainToken(
            token.accessToken,
            token.tokenType,
            token.expiresIn
        )

    fun mapUser(user: User) =
        DomainUser(
            user.email,
            user.login,
            user.fullName,
            user.imageUrl,
            user.location,
            user.points,
            user.wallet,
            mapProjects(user.projects),
            mapCampus(user.campuses)
        )

    private fun mapProjects(projects: List<Project>): List<DomainProject> =
        projects.map {
            DomainProject(
                it.base.id,
                it.base.parentId?.toInt(),
                it.courseIds,
                it.base.name,
                it.finalMark,
                it.status,
                it.isValidated
            )
        }

    fun mapCourses(courses: List<Course>): List<DomainCourse> =
        courses.map {
            DomainCourse(
                it.id,
                it.grade ?: GRADE_NOVICE,
                it.base.name,
                it.level,
                mapSkills(it.skills)
            )
        }

    private fun mapSkills(skills: List<Skill>): List<DomainSkill> =
        skills.map {
            DomainSkill(
                it.name,
                it.level
            )
        }

    private fun mapCampus(campuses: List<Campus>): List<DomainCampus> =
        campuses.map {
            DomainCampus(
                it.name,
                it.active,
                it.city,
            )
        }

    fun mapCoalitionIds(coalitionIds: List<CoalitionId>): List<DomainCoalitionId> =
        coalitionIds.map {
            DomainCoalitionId(
                it.id
            )
        }

    fun mapCoalitions(coalitionList: List<Coalition>): List<DomainCoalition> =
        coalitionList.map {
            DomainCoalition(
                it.id,
                it.name,
                it.imageUrl,
                it.backgroundUrl,
                ("ff" + it.color.trim('#')).toLong(16)
            )
        }

    companion object {
        private const val GRADE_NOVICE = "Novice"
    }
}