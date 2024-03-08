package com.example.decadeofmovies

import android.app.Application
import android.content.Context
import dagger.Provides
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MoviesApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        instance = this

    }

    companion object {
        lateinit var instance: MoviesApplication
            private set
    }

}