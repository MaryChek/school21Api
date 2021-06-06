package com.example.a42_api.domain.models

class Token(
    val accessToken: String,
    val tokenType: String,
    val expiresIn: Int
)