package com.connor.fuckcolorapp.extension

import android.app.Activity
import android.app.Service
import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.connor.fuckcolorapp.BuildConfig
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

fun Any.logCat(tab: String = "FUCK_COLOR_LOG") {
    if (!BuildConfig.DEBUG) return
    if (this is String) Log.d(tab, this) else Log.d(tab, this.toString())
}

fun String.getCurrentThread() {
    "$this: ${Thread.currentThread().name}".logCat()
}

fun String.showToast(context: Context) {
    Toast.makeText(context, this, Toast.LENGTH_SHORT).show()
}

fun Context.showToast(string: String) {
    string.showToast(this)
}

fun String.showSnackbar(view: View) {
    Snackbar.make(view, this, Snackbar.LENGTH_SHORT).show()
}

fun EditText.textChanges() = callbackFlow {
    val listener = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            p0?.let { trySend(it) }
        }

        override fun afterTextChanged(p0: Editable?) {

        }
    }
    addTextChangedListener(listener)
    awaitClose {
        removeTextChangedListener(listener)
    }
}

inline fun <reified T> Context.intent(builder: Intent.() -> Unit = {}): Intent =
    Intent(this, T::class.java).apply(builder)

inline fun <reified T : Service> Context.startService(block: Intent.() -> Unit = {}) =
    startService(intent<T>(block))

inline fun <reified T : Activity> Context.startActivity(block: Intent.() -> Unit = {}) =
    startActivity(intent<T>(block))

inline fun Fragment.repeatOnStart(crossinline block: CoroutineScope.() -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            block()
        }
    }
}