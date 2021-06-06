package com.example.a42_api.data.api.models

import com.squareup.moshi.Json

class Coalition(
    val id: Int,
    val name: String,
    @field:Json(name = "image_url")
    val imageUrl: String,
    @field:Json(name = "cover_url")
    val backgroundUrl: String?,
    val color: String
)