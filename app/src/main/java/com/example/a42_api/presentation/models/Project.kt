package com.example.a42_api.presentation.models

import androidx.annotation.ColorRes
import com.example.a42_api.R

class Project(
    val id: Int,
    val parentId: Int?,
    val courseIds: List<Int>,
    val name: String,
    val finalMark: Int?,
    val status: String,
    isValidated: Boolean?
) {
    val isInProgressVisible: Boolean = isValidated == null
    val isInFailVisible: Boolean = isValidated == false
    val isInSuccessVisible: Boolean = isValidated == true
    val isFinalMarkVisible: Boolean = !isInProgressVisible

    @ColorRes
    val finalMarkColorResId =
        when (isValidated == true) {
            true -> R.color.success_final_mark
            false -> R.color.fail_final_mark
        }
}