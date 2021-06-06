package com.example.a42_api.domain.models

class Project(
    val id: Int,
    val parentId: Int?,
    val courseIds: List<Int>,
    val name: String,
    val finalMark: Int?,
    val status: String,
    val isValidated: Boolean?
)