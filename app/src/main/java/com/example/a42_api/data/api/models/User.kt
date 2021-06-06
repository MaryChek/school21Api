package com.example.a42_api.data.api.models

import com.squareup.moshi.Json

class User(
    val email: String,
    val login: String,
    @field:Json(name = "displayname")
    val fullName: String,
    @field:Json(name = "image_url")
    val imageUrl: String,
    val location: String?,
    @field:Json(name = "correction_point")
    val points: Int,
    val wallet: Int,
    @field:Json(name = "projects_users")
    val projects: List<Project>,
    @field:Json(name = "campus")
    val campuses: List<Campus>
)
