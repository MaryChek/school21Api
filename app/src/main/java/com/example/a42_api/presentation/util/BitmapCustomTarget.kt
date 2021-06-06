package com.example.a42_api.presentation.util

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

open class BitmapCustomTarget : CustomTarget<Bitmap>() {
    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {}

    override fun onLoadCleared(placeholder: Drawable?) {}
}