package com.example.decadeofmovies.repo

import com.example.decadeofmovies.model.Movie

interface MovieRepo {

    suspend fun getMoviesList(): List<Movie>?
}