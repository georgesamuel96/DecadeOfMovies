package com.example.decadeofmovies.ui.adapter

import android.content.Context
import com.example.decadeofmovies.model.Movie

sealed class AdapterMovieTypes {
    data class ItemYear(val year: Int) : AdapterMovieTypes()

    data class ItemMovie(
        val context: Context,
        val movie: Movie,
        val movieListClickListener: MovieAdapter.MovieListClickListener
    ) : AdapterMovieTypes()
}