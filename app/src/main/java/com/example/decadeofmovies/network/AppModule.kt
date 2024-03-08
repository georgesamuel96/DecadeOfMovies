package com.example.decadeofmovies.network

import android.app.Application
import android.content.Context
import com.example.decadeofmovies.repo.MovieRepo
import com.example.decadeofmovies.repo.MovieRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideAppContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideMovieRepo(context: Context): MovieRepo = MovieRepoImpl(context)
}
