package com.example.decadeofmovies.repo

import com.example.decadeofmovies.model.FlickrSearchPhotoResponse
import com.example.decadeofmovies.model.Movie
import retrofit2.Response

class FakeMovieRepo: MovieRepo {

    private val moviesList = mutableListOf(
        Movie(
            "(500) Days of Summer",
            2009,
            listOf(
                "Joseph Gordon-Levitt",
                "Zooey Deschanel"
            ),
            listOf(
                "Romance",
                "Comedy"
            ),
            1
        ),
        Movie(
            "Crazy, Stupid, Love.",
            2011,
            listOf(
                "Steve Carell",
                "Ryan Gosling",
                "Julianne Moore",
                "Emma Stone",
                "John Carroll Lynch",
                "Marisa Tomei",
                "Kevin Bacon",
                "Analeigh Tipton",
                "Liza Lapira",
                "Joey King",
                "Mekia Cox"
            ),
            listOf(
                "Comedy"
            ),
            4
        )
    )

    override suspend fun getMoviesList(): List<Movie> {
        return moviesList
    }

    override suspend fun getSearchPhotos(title: String): Response<FlickrSearchPhotoResponse>? {
        return null
    }
}