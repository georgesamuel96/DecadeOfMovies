package com.example.decadeofmovies.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.decadeofmovies.getOrAwaitValueTest
import com.example.decadeofmovies.repo.FakeMovieRepo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MovieViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()


    private lateinit var fakeMovieRepo: FakeMovieRepo
    private lateinit var viewModel: MovieViewModel

    @Before
    fun setup() {
        fakeMovieRepo = FakeMovieRepo()
        viewModel = MovieViewModel(fakeMovieRepo)
    }

    @Test
    fun test_searchOnMovieReturnList() = runBlocking {
        viewModel.searchOnMovie("500", fakeMovieRepo.getMoviesList())
        delay(100)
        val value = viewModel.movieListLiveData.getOrAwaitValueTest()
        assertEquals(2, value.data?.size)
    }

    @Test
    fun test_searchOnMovieReturnEmptyList() = runBlocking {
        viewModel.searchOnMovie("22", fakeMovieRepo.getMoviesList())
        delay(100)
        val value = viewModel.movieListLiveData.getOrAwaitValueTest()
        assertEquals(0, value.data?.size)
    }

    @Test
    fun test_searchOnMovieEmptyStringReturnList() = runBlocking {
        viewModel.searchOnMovie("", fakeMovieRepo.getMoviesList())
        delay(100)
        val value = viewModel.movieListLiveData.getOrAwaitValueTest()
        assertEquals(2, value.data?.size)
    }

    @Test
    fun test_searchOnMovieReturnAllMovies() = runBlocking {
        viewModel.searchOnMovie("a", fakeMovieRepo.getMoviesList())
        delay(100)
        val value = viewModel.movieListLiveData.getOrAwaitValueTest()
        assertEquals(4, value.data?.size)
    }
}