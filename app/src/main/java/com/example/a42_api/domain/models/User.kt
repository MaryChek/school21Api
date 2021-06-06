package com.example.a42_api.domain.models

class User(
    val email: String,
    val login: String,
    val fullName: String,
    val imageUrl: String,
    val location: String?,
    val points: Int,
    val wallet: Int,
    val projects: List<Project>,
    val campuses: List<Campus>
)