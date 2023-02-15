package com.connor.fuckcolorapp.extension

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.connor.fuckcolorapp.App
import com.connor.fuckcolorapp.BuildConfig

fun Any.logCat(tab: String = "FUCK_COLOR_LOG") {
    if (!BuildConfig.DEBUG) return
    if (this is String) Log.d(tab, this) else Log.d(tab, this.toString())
}

fun String.showToast() {
    Toast.makeText(App.app.applicationContext, this, Toast.LENGTH_SHORT).show()
}

inline fun <reified T> Context.startService(block: Intent.() -> Unit) {
    val intent = Intent(this, T::class.java)
    intent.block()
    startService(intent)
}

inline fun <reified T> Context.startActivity(block: Intent.() -> Unit) {
    val intent = Intent(this, T::class.java)
    intent.block()
    startActivity(intent)
}