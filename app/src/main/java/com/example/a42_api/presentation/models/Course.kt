package com.example.a42_api.presentation.models

class Course(
    val grade: String,
    val name: String,
    val level: Level,
    val coalition: Coalition,
    val skills: List<Skill>,
    val projects: List<Project>,
) {
    class Level(
        val fullLevel: Int,
        val percentage: Int
    )
}