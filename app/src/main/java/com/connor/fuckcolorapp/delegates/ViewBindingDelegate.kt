package com.connor.fuckcolorapp.delegates

import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class ViewBindingDelegate<T : ViewBinding>(
    private val bindingClass: Class<T>,
    private val fragment: Fragment,
) : ReadOnlyProperty<Fragment, T> {

    private var _binding: T? = null

    init {
        @Suppress("UNCHECKED_CAST")
        fragment.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onCreate(owner: LifecycleOwner) {
                super.onCreate(owner)
                _binding = bindingClass.getMethod("inflate", LayoutInflater::class.java)
                    .invoke(null, fragment.layoutInflater) as T
            }

            override fun onDestroy(owner: LifecycleOwner) {
                super.onDestroy(owner)
                _binding = null
            }
        })
    }

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        return _binding ?: throw IllegalStateException("ViewBinding is not initialized.")
    }
}