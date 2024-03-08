package com.example.decadeofmovies.model

data class Movie(
    val title: String?,
    val year: Int?,
    val cast: List<String>?,
    val genres: List<String>?,
    val rating: Int?
)
