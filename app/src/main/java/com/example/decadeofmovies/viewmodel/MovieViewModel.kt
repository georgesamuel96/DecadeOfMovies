package com.example.decadeofmovies.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.decadeofmovies.model.Movie
import com.example.decadeofmovies.network.Resource
import com.example.decadeofmovies.repo.MovieRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val movieRepo: MovieRepo
) : ViewModel() {

    private val startLoading = MutableLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean> = startLoading

    private val moviesList = MutableLiveData<Resource<List<Movie>>>()
    val movieListLiveData: LiveData<Resource<List<Movie>>> = moviesList

    private val emptyMoviesList = MutableLiveData<Boolean>()
    val emptyMoviesListLiveData: LiveData<Boolean> = emptyMoviesList


    fun getMoviesList() {
        viewModelScope.launch(Dispatchers.IO) {
            startLoading.postValue(true)

            withContext(Dispatchers.Main) {
                val list = movieRepo.getMoviesList()
                if(list.isNullOrEmpty()) {
                    emptyMoviesList.value = true
                    moviesList.value = Resource.success(listOf())
                }
                else {
                    emptyMoviesList.value = false
                    moviesList.value = Resource.success(list)
                }
            }

            startLoading.postValue(false)
        }
    }
}