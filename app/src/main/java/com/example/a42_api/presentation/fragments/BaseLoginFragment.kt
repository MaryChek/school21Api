package com.example.a42_api.presentation.fragments

import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.transition.Transition
import com.example.a42_api.R
import com.example.a42_api.presentation.util.BitmapCustomTarget
import com.example.a42_api.presentation.util.GlideSvgLoadListener
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou

open class BaseLoginFragment : Fragment() {

    protected fun View.updateVisibility(show: Boolean) {
        val visibility =
            when (show) {
                true -> View.VISIBLE
                else -> View.GONE
            }
        this.visibility = visibility
    }

    protected fun setColorToTextByRes(view: TextView, @ColorRes colorId: Int) {
        view.setTextColor(getColor(colorId))
    }

    protected fun changeLayerPainForImageView(view: ImageView, color: Long? = null) {
        val paint = Paint()
        val currentColor = getCurrentColor(color)
        val colorFilter = getColorFilter(currentColor, PorterDuff.Mode.SRC_ATOP)
        paint.colorFilter = colorFilter
        view.setLayerPaint(paint)
    }

    protected fun changeColorForImageIcon(view: ImageView, color: Long? = null) {
        val currentColor = getCurrentColor(color)
        view.colorFilter = getColorFilter(currentColor, PorterDuff.Mode.SRC_IN)
    }

    protected fun changeColorForDrawable(drawable: Drawable, color: Long? = null) {
        val currentColor = getCurrentColor(color)
        drawable.colorFilter = getColorFilter(currentColor, PorterDuff.Mode.SRC_IN)
    }

    private fun getCurrentColor(color: Long?): Int =
        (color ?: getColor(R.color.white)).toInt()

    private fun getColorFilter(color: Int, PorterDuffMode: PorterDuff.Mode) =
        PorterDuffColorFilter(color, PorterDuffMode)

    protected fun setImageToBackgroundFromUrl(imageView: ImageView, imageUrl: String) {
        Glide.with(this)
            .asBitmap()
            .load(imageUrl)
            .into(object : BitmapCustomTarget() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    val drawable: Drawable = BitmapDrawable(resources, resource)
                    imageView.background = drawable
                }
            })
    }

    protected fun setSvgToImageViewWithCallBackFuncIfRequired(
        imageView: ImageView,
        imageUrl: String,
        onImageLoad: ((ImageView) -> Unit)? = null
    ) {
        val uri: Uri = Uri.parse(imageUrl)
        GlideToVectorYou
            .init()
            .with(requireActivity())
            .withListener(object : GlideSvgLoadListener() {
                override fun onResourceReady() {
                    onImageLoad?.invoke(imageView)
                }
            })
            .load(uri, imageView)
    }

    protected fun getColor(@ColorRes colorId: Int): Int =
        resources.getColor(colorId, requireActivity().theme)

    protected fun getDrawable(@DrawableRes drawableResId: Int): Drawable? =
        ResourcesCompat.getDrawable(resources, drawableResId, requireActivity().theme)
}