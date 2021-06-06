package com.example.a42_api.domain.models

class Course(
    val id: Int,
    val grade: String,
    val name: String,
    val level: Double,
    val skills: List<Skill>
)