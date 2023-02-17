package com.connor.fuckcolorapp.di

import android.app.Application
import com.connor.fuckcolorapp.App
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Provides
    fun provideApp(application: Application): App {
        return application as App
    }
}