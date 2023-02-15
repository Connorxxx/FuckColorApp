package com.connor.fuckcolorapp.di

import com.connor.fuckcolorapp.models.AppInfo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppListModule {

    @Singleton
    @get:Provides
    val appList = ArrayList<AppInfo>()
}