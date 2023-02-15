package com.connor.fuckcolorapp.models

import android.graphics.drawable.Drawable

data class AppInfo(
    var label: CharSequence? = "app",
    var packageName: CharSequence? = "com",
    var icon: Drawable? = null
)