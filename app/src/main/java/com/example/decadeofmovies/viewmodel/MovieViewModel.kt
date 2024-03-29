package com.example.decadeofmovies.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.decadeofmovies.model.FlickrPhoto
import com.example.decadeofmovies.model.Movie
import com.example.decadeofmovies.network.EmptyMovieListStatus
import com.example.decadeofmovies.network.Resource
import com.example.decadeofmovies.repo.MovieRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val movieRepo: MovieRepo
) : ViewModel() {

    private val startLoading = MutableLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean> = startLoading

    private val moviesListResponse = MutableLiveData<Resource<List<Movie>>>()
    val movieListLiveData: LiveData<Resource<List<Movie>>> = moviesListResponse

    private val emptyMoviesList = MutableLiveData<EmptyMovieListStatus>()
    val emptyMoviesListLiveData: LiveData<EmptyMovieListStatus> = emptyMoviesList

    private val openMovieData = MutableLiveData<Movie>()
    val openMovieLiveData: LiveData<Movie> = openMovieData

    private val moviePhotos = MutableLiveData<Resource<List<FlickrPhoto>>>()
    val moviePhotosLiveData: LiveData<Resource<List<FlickrPhoto>>> = moviePhotos

    private val emptyMoviePhotos = MutableLiveData<Boolean>()
    val emptyMoviePhotosLiveData: LiveData<Boolean> = emptyMoviePhotos

    private var jobSearch: Job? = null
    private val moviesListSearchIndex = Array(2025) { IntArray(6) { 0 } }

    fun getMoviesList() {
        viewModelScope.launch(Dispatchers.IO) {
            startLoading.postValue(true)

            withContext(Dispatchers.Main) {
                val list = movieRepo.getMoviesList()

                if (list.isNullOrEmpty()) {
                    emptyMoviesList.value = EmptyMovieListStatus.REQUEST
                    moviesListResponse.value = Resource.success(listOf())
                } else {
                    emptyMoviesList.value = EmptyMovieListStatus.NONE
                    moviesListResponse.value = Resource.success(list)
                }
            }

            startLoading.postValue(false)
        }
    }

    /**
     * This function search on movie by its title
     * I am using job coroutines to cancel the job (cancel the previous search)
     * when the user write new character to search
     */
    fun searchOnMovie(text: String, moviesList: List<Movie>) {
        jobSearch?.cancel()

        if (text.isEmpty()) {
            setMoviesListData(moviesList)
            return
        }

        var title: String
        var year: Int
        val newFilteredMovieList = mutableListOf<Movie>()
        val sortedList = mutableListOf<Movie>()

        jobSearch = viewModelScope.launch(Dispatchers.IO) {

            sortedList.addAll(moviesList.sortedByDescending { it.rating })

            resetMoviesListSearchIndex()

            // get the most rated movies according to each year
            for (movie in sortedList.withIndex()) {
                title = (movie.value.title ?: "").lowercase()
                if (title.contains(text)) {
                    year = movie.value.year ?: 0
                    if (moviesListSearchIndex[year][0] < 5) {
                        moviesListSearchIndex[year][0]++
                        moviesListSearchIndex[year][moviesListSearchIndex[year][0]] = movie.index
                    }
                }
            }

            for (yearIndex in 0 until 2025) {
                for (movieIndex in 1..moviesListSearchIndex[yearIndex][0]) {
                    checkAddYearHeader(newFilteredMovieList, yearIndex)
                    newFilteredMovieList.add(sortedList[moviesListSearchIndex[yearIndex][movieIndex]])
                }
            }

            setMoviesListData(newFilteredMovieList)
        }
    }

    private fun checkAddYearHeader(newFilteredMovieList: MutableList<Movie>, year: Int) {
        if (newFilteredMovieList.isEmpty().not()) {
            if (newFilteredMovieList.last().year != year) {
                newFilteredMovieList.add(Movie(year = year, rating = -1))
            }
        } else {
            newFilteredMovieList.add(Movie(year = year, rating = -1))
        }
    }

    private fun setMoviesListData(newFilteredMovieList: List<Movie>) {
        emptyMoviesList.postValue(
            if (newFilteredMovieList.isEmpty()) EmptyMovieListStatus.SEARCH else EmptyMovieListStatus.NONE
        )

        moviesListResponse.postValue(Resource.success(newFilteredMovieList))
    }

    //Reset the indexes of the top rated movies
    private fun resetMoviesListSearchIndex() {
        for (index in 0 until 2025) {
            moviesListSearchIndex[index][0] = 0
        }
    }

    fun openMovieLiveData(movie: Movie) {
        openMovieData.value = movie
    }

    fun getPhotos(title: String) {
        viewModelScope.launch(Dispatchers.IO) {
            startLoading.postValue(true)

            val response = movieRepo.getSearchPhotos(title)
            if(response?.isSuccessful == true) {
                val photos = response.body()?.photos?.photo
                if(photos.isNullOrEmpty()) {
                    emptyMoviePhotos.postValue(true)
                } else {
                    emptyMoviePhotos.postValue(false)
                    moviePhotos.postValue(Resource.success(photos))
                }
            } else {
                moviePhotos.postValue(Resource.error(""))
            }

            startLoading.postValue(false)
        }
    }
}