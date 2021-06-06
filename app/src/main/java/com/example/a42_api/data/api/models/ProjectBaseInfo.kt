package com.example.a42_api.data.api.models

import com.squareup.moshi.Json

class ProjectBaseInfo(
    val id: Int,
    val name: String,
    @field:Json(name = "parent_id")
    val parentId: String?
)