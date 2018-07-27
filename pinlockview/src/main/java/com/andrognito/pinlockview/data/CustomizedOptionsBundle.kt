package com.andrognito.pinlockview.data

import android.graphics.drawable.Drawable

data class CustomizedOptionsBundle(
    var pinLength: Int = 0,
    var textColor: Int = 0,
    var textSize: Int = 0,
    var buttonSize: Int = 0,
    var buttonBackgroundDrawable: Drawable? = null,
    var deleteButtonDrawable: Drawable? = null,
    var deleteButtonSize: Int = 0,
    var showDeleteButton: Boolean = false,
    var deleteButtonPressesColor: Int = 0
)
