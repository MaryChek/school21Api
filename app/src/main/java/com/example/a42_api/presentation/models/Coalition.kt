package com.example.a42_api.presentation.models

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.example.a42_api.R

class Coalition(
    val imageUrl: String? = null,
    val backgroundUrl: String? = null,
    val colorProfile: Long? = null
) {
    @DrawableRes
    val defaultBackgroundResId: Int = R.drawable.background

    @ColorRes
    val defaultColorResId: Int = R.color.default_coalition_color
}