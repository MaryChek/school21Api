package com.example.a42_api.presentation.models

import androidx.annotation.ColorRes
import com.example.a42_api.R

class Skill(
    val name: String,
    val level: Double
) {
    val levelProgress: Int = level.div(21).times(100).toInt()

    @ColorRes
    val progressTextColorResId = R.color.black
}