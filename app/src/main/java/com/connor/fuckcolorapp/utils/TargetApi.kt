package com.connor.fuckcolorapp.utils

import android.os.Build

object TargetApi {
    private fun target(api: Int): Boolean = Build.VERSION.SDK_INT >= api

    val O = target(Build.VERSION_CODES.O)
    val P = target(Build.VERSION_CODES.P)
    val Q = target(Build.VERSION_CODES.Q)
    val T = target(Build.VERSION_CODES.TIRAMISU)
}