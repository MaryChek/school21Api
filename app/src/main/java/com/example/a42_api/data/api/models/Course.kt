package com.example.a42_api.data.api.models

import com.squareup.moshi.Json

class Course(
    @field:Json(name = "cursus_id")
    val id: Int,
    val grade: String?,
    val level: Double,
    val skills: List<Skill>,
    @field:Json(name = "cursus")
    val base: CourseBaseInfo
)