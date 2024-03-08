package com.example.decadeofmovies.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.decadeofmovies.R
import com.example.decadeofmovies.databinding.FragmentMoviesListBinding
import com.example.decadeofmovies.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoviesListFragment : Fragment() {

    private var _binding: FragmentMoviesListBinding? = null
    private val movieViewModel: MovieViewModel by viewModels()

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        movieViewModel.getMoviesList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMoviesListBinding.inflate(inflater, container, false)

        initializeViewModelObservers()

        return binding.root
    }

    private fun initializeViewModelObservers() {
        getMoviesListObserver()
    }

    private fun getMoviesListObserver() {
        movieViewModel.movieListLiveData.observe(viewLifecycleOwner) {
            binding.tv.text = "${it?.size?: -1}"
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}