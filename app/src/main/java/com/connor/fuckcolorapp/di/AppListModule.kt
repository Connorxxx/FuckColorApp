package com.connor.fuckcolorapp.di

import com.connor.fuckcolorapp.models.AppInfo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppListModule {

//    @Singleton
//    @UserList
//    @Provides
//    fun appList() = ArrayList<AppInfo>()
//
//    @Singleton
//    @SystemList
//    @Provides
//    fun systemAppList() = ArrayList<AppInfo>()
}

//@Qualifier
//@Retention(AnnotationRetention.RUNTIME)
//annotation class UserList
//
//@Qualifier
//@Retention(AnnotationRetention.RUNTIME)
//annotation class SystemList