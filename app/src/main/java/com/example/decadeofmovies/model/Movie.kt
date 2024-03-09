package com.example.decadeofmovies.model

data class Movie(
    val title: String? = null,
    val year: Int?,
    val cast: List<String>? = null,
    val genres: List<String>? = null,
    val rating: Int?
)
