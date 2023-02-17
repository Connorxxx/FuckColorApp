package com.connor.fuckcolorapp.extension

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import com.connor.fuckcolorapp.BuildConfig
import com.google.android.material.snackbar.Snackbar

fun Any.logCat(tab: String = "FUCK_COLOR_LOG") {
    if (!BuildConfig.DEBUG) return
    if (this is String) Log.d(tab, this) else Log.d(tab, this.toString())
}

fun String.showToast(context: Context) {
    Toast.makeText(context, this, Toast.LENGTH_SHORT).show()
}

fun String.showSnackbar(view: View) {
    Snackbar.make(view, this, Snackbar.LENGTH_SHORT).show()
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