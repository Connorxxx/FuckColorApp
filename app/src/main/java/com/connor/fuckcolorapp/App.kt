package com.connor.fuckcolorapp

import android.app.Application
import com.connor.fuckcolorapp.models.AppInfo
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

   // companion object {
      //  lateinit var app: App private set
   //}
    val userAppList by lazy { ArrayList<AppInfo>() }
    val systemAppList by lazy { ArrayList<AppInfo>() }
    val allAppList by lazy { ArrayList<AppInfo>() }


    override fun onCreate() {
        super.onCreate()
      //  app = this
    }

}