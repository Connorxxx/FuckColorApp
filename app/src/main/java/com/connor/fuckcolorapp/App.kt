package com.connor.fuckcolorapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    companion object {
        lateinit var app: App private set
    }

    override fun onCreate() {
        super.onCreate()
        app = this
    }

}