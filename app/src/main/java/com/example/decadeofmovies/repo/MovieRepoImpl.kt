package com.example.decadeofmovies.repo

import android.content.Context
import com.example.decadeofmovies.model.FlickrSearchPhotoResponse
import com.example.decadeofmovies.model.Movie
import com.example.decadeofmovies.network.APIService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.io.IOException
import java.io.InputStream

class MovieRepoImpl(
    private val context: Context,
    private val apiService: APIService
) : MovieRepo {
    override suspend fun getMoviesList(): List<Movie>? {
        try {
            val obj = JSONObject(loadJSONFromAsset())
            val data: JSONArray = obj.getJSONArray("movies")
            val moviesList = mutableListOf<Movie>()
            for (movieIndex in 0 until data.length()) {
                val movie = data.getJSONObject(movieIndex)

                val title = movie.getString("title")
                val year = movie.getInt("year")
                val castList = getListNames(movie.getJSONArray("cast"))
                val genresList = getListNames(movie.getJSONArray("genres"))
                val rating = movie.getInt("rating")

                moviesList.add(
                    Movie(
                        title,
                        year,
                        castList,
                        genresList,
                        rating
                    )
                )
            }

            return moviesList
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return null
    }

    private fun getListNames(data: JSONArray): List<String> {
        val dataList = mutableListOf<String>()
        for (nameIndex in 0 until data.length()) {
            val name = data.getString(nameIndex)
            dataList.add(name)
        }
        return dataList
    }

    private suspend fun loadJSONFromAsset(): String? {
        val json: String = try {
            withContext(Dispatchers.IO) {
                val inputSystem: InputStream = context.assets.open("movies.json")
                val size: Int = inputSystem.available()
                val buffer = ByteArray(size)
                inputSystem.read(buffer)
                inputSystem.close()
                String(buffer, Charsets.UTF_8)
            }
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }

    override suspend fun getSearchPhotos(title: String): Response<FlickrSearchPhotoResponse>? {
        return apiService.searchFlickrPhotos(
            "flickr.photos.search",
            "47a6658cb4d2de59f073d0c89af91c59",
            "json",
            title,
            "1"
        )
    }
}