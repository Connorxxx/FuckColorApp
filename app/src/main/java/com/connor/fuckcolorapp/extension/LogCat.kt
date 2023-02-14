package com.connor.fuckcolorapp.extension

import android.util.Log
import com.connor.fuckcolorapp.BuildConfig

fun Any.logCat(tab: String = "FUCK_COLOR_LOG") {
    if (!BuildConfig.DEBUG) return
    if (this is String) Log.d(tab, this) else Log.d(tab, this.toString())
}