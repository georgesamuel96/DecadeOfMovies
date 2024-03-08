package com.example.decadeofmovies

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FleetxApplication: Application() {

    override fun onCreate() {
        super.onCreate()
    }
}