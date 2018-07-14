package com.andrognito.pinlockview.util

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.annotation.ColorRes
import android.support.annotation.DimenRes
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat

object ResourceUtils {
    fun getColor(context: Context, @ColorRes id: Int): Int {
        return ContextCompat.getColor(context, id)
    }

    fun getDimensionInPx(context: Context, @DimenRes id: Int): Float {
        return context.resources.getDimension(id)
    }

    fun getDrawable(context: Context, @DrawableRes id: Int): Drawable? {
        return ContextCompat.getDrawable(context, id)
    }
}
