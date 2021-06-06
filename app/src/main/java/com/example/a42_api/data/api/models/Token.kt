package com.example.a42_api.data.api.models

import com.squareup.moshi.Json

class Token(
    @field:Json(name = "access_token")
    val accessToken: String,
    @field:Json(name = "token_type")
    val tokenType: String,
    @field:Json(name = "expires_in")
    val expiresIn: Int
)