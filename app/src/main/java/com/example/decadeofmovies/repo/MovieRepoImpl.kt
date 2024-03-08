package com.example.decadeofmovies.repo

import android.content.Context
import com.example.decadeofmovies.model.Movie
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream

class MovieRepoImpl(
    private val context: Context
): MovieRepo {
    override fun getMoviesList(): List<Movie>? {
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
        for(nameIndex in 0 until data.length()) {
            val name = data.getString(nameIndex)
            dataList.add(name)
        }
        return dataList
    }

    private fun loadJSONFromAsset(): String? {
        val json: String = try {
            val inputSystem: InputStream = context.assets.open("movies.json")
            val size: Int = inputSystem.available()
            val buffer = ByteArray(size)
            inputSystem.read(buffer)
            inputSystem.close()
            String(buffer, Charsets.UTF_8)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }
}