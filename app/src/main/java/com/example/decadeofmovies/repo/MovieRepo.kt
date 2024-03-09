package com.example.decadeofmovies.repo

import com.example.decadeofmovies.model.FlickrSearchPhotoResponse
import com.example.decadeofmovies.model.Movie
import retrofit2.Response

interface MovieRepo {

    suspend fun getMoviesList(): List<Movie>?
    suspend fun getSearchPhotos(title: String): Response<FlickrSearchPhotoResponse>?
}