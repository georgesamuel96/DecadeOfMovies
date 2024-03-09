package com.example.decadeofmovies.network

import com.example.decadeofmovies.model.FlickrSearchPhotoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {

    @GET("/services/rest/")
    suspend fun searchFlickrPhotos(
        @Query("method") method: String,
        @Query("api_key") apiKey: String,
        @Query("format") format: String,
        @Query("text") text: String,
        @Query("nojsoncallback") nojsoncallback: String
    ): Response<FlickrSearchPhotoResponse>?
}