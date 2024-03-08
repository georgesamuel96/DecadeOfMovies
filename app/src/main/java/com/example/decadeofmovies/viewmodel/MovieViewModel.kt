package com.example.decadeofmovies.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.decadeofmovies.model.Movie
import com.example.decadeofmovies.repo.MovieRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val movieRepo: MovieRepo
) : ViewModel() {

    private val moviesList = MutableLiveData<List<Movie>?>()
    val movieListLiveData: LiveData<List<Movie>?> = moviesList

    fun getMoviesList() {
        viewModelScope.launch {
            moviesList.value = movieRepo.getMoviesList()
        }
    }
}