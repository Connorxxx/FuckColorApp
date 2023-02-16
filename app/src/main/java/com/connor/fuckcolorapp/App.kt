package com.connor.fuckcolorapp

import android.app.Application
import com.connor.fuckcolorapp.models.AppInfo
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    companion object {
        lateinit var app: App private set
        lateinit var userAppList: ArrayList<AppInfo> private set
        lateinit var systemAppList: ArrayList<AppInfo> private set
    }

    override fun onCreate() {
        super.onCreate()
        app = this
        userAppList = ArrayList()
        systemAppList = ArrayList()
    }

}