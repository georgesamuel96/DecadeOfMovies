package com.example.decadeofmovies.repo

import com.example.decadeofmovies.model.Movie

interface MovieRepo {

    fun getMoviesList(): List<Movie>?
}