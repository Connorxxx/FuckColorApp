package com.connor.fuckcolorapp.models

import android.graphics.drawable.Drawable
import androidx.databinding.BaseObservable

data class AppInfo(
    var label: CharSequence? = "app",
    val packageName: CharSequence? = "com",
    val icon: Drawable? = null,
    var isUserApp: Boolean,
    var isCheck: Boolean = false,
) : BaseObservable()