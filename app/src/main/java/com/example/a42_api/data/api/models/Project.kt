package com.example.a42_api.data.api.models

import com.squareup.moshi.Json

class Project(
    @field:Json(name = "final_mark")
    val finalMark: Int?,
    val status: String,
    @field:Json(name = "validated?")
    val isValidated: Boolean?,
    @field:Json(name = "project")
    val base: ProjectBaseInfo,
    @field:Json(name = "cursus_ids")
    val courseIds: List<Int>,
)