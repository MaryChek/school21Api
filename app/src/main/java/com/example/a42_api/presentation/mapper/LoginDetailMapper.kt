package com.example.a42_api.presentation.mapper

import com.example.a42_api.BaseMapper
import com.example.a42_api.presentation.models.Coalition
import com.example.a42_api.presentation.models.Course
import com.example.a42_api.presentation.models.Project
import com.example.a42_api.presentation.models.Skill
import com.example.a42_api.domain.models.Course as DomainCourse
import com.example.a42_api.domain.models.Skill as DomainSkill
import com.example.a42_api.domain.models.Coalition as DomainCoalition

class LoginDetailMapper : BaseMapper() {

    fun mapCourses(
        courses: List<DomainCourse>,
        coalitions: List<DomainCoalition>,
        projects: List<Project>
    ): List<Course> =
        courses.zip(mapCoalition(coalitions, courses.size)).map { (course, coalition) ->
            mapCourse(course, coalition, mapProjectsByCourseId(projects, course.id))
        }

    private fun mapCoalition(
        coalitions: List<DomainCoalition>,
        coursesSize: Int
    ): List<Coalition> {
        val currentCoalition = mapCoalitions(coalitions)
        return if (currentCoalition.size < coursesSize) {
            currentCoalition + Coalition()
        } else {
            currentCoalition
        }
    }

    private fun mapProjectsByCourseId(projects: List<Project>, courseId: Int): List<Project> =
        projects.filter { project ->
            project.courseIds.contains(courseId) && project.parentId == null
        }

    private fun mapCourse(
        course: DomainCourse,
        coalition: Coalition,
        project: List<Project>
    ): Course =
        Course(
            course.grade,
            course.name,
            mapLevel(course.level),
            coalition,
            mapSkills(course.skills),
            project
        )

    private fun mapLevel(level: Double): Course.Level {
        val levelAndPercent = level.toString().split('.')
        return when {
            levelAndPercent.size > 1 -> {
                Course.Level(
                    levelAndPercent[0].toInt(),
                    levelAndPercent[1].toInt()
                )
            }
            levelAndPercent.size == 1 -> {
                Course.Level(
                    levelAndPercent[0].toInt(),
                    0
                )
            }
            else -> {
                Course.Level(
                    0,
                    0
                )
            }
        }
    }


    private fun mapCoalitions(coalitions: List<DomainCoalition>): List<Coalition> =
        coalitions.map { coalition ->
            Coalition(
                coalition.imageUrl,
                coalition.backgroundUrl,
                coalition.color
            )
        }

    private fun mapSkills(skills: List<DomainSkill>): List<Skill> =
        skills.map { skill ->
            Skill(
                firstUpperCase(skill.name),
                skill.level
            )
        }
}