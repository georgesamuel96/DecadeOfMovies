package com.example.decadeofmovies.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.decadeofmovies.R
import com.example.decadeofmovies.databinding.FragmentMovieDetailsBinding
import com.example.decadeofmovies.model.Movie
import com.example.decadeofmovies.network.Status
import com.example.decadeofmovies.ui.adapter.MemberAdapter
import com.example.decadeofmovies.viewmodel.MovieViewModel

class MovieDetailsFragment : Fragment() {

    private var _binding: FragmentMovieDetailsBinding? = null
    private val movieViewModel: MovieViewModel by activityViewModels()
    private lateinit var movie: Movie

    private val binding get() = _binding!!

    private val membersAdapter: MemberAdapter by lazy {
        MemberAdapter(mutableListOf())
    }
    private val genresAdapter: MemberAdapter by lazy {
        MemberAdapter(mutableListOf())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)

        initialization()
        initializeViewModelObservers()
        listeners()

        return binding.root
    }

    private fun listeners() {
    }

    private fun initializeViewModelObservers() {
        getOpeningMovieObserver()
        getMoviePhotosObserver()
    }

    private fun getMoviePhotosObserver() {
        movieViewModel.moviePhotosLiveData.observe(viewLifecycleOwner) {
            when(it.status) {
                Status.SUCCESS -> {
                    Toast.makeText(requireContext(), "${it.data?.size}", Toast.LENGTH_SHORT).show()
                }
                Status.ERROR -> {

                }
            }
        }
    }

    private fun getOpeningMovieObserver() {
        movieViewModel.openMovieLiveData.observe(viewLifecycleOwner) {
            movie = it
            setMovieDetails()
        }
    }

    private fun setMovieDetails() {
        binding.apply {
            tvTitle.text = movie.title ?: ""
            tvRating.text =
                if (movie.rating == null) "" else getString(R.string.rating, "${movie.rating}")
            tvYear.text =
                if (movie.year == null) "" else getString(R.string.year, "${movie.year}")

            if (movie.cast.isNullOrEmpty()) {
                groupCastMembers.visibility = View.GONE
            } else {
                View.VISIBLE
            }

            if (movie.genres.isNullOrEmpty()) {
                groupGenres.visibility = View.VISIBLE
            } else {
                groupGenres.visibility = View.GONE
            }

            membersAdapter.updateList(movie.cast ?: listOf())
            genresAdapter.updateList(movie.genres ?: listOf())
            movie.title?.let {
                movieViewModel.getPhotos(it)
            }
        }
    }

    private fun initialization() {
        binding.apply {
            rvCastMembers.isNestedScrollingEnabled = false
            rvCastMembers.adapter = membersAdapter

            rvGenres.isNestedScrollingEnabled = false
            rvGenres.adapter = genresAdapter
        }
    }

}