package com.example.decadeofmovies.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.decadeofmovies.R
import com.example.decadeofmovies.databinding.FragmentMovieDetailsBinding
import com.example.decadeofmovies.model.Movie
import com.example.decadeofmovies.network.Status
import com.example.decadeofmovies.ui.adapter.MemberAdapter
import com.example.decadeofmovies.ui.adapter.PhotoAdapter
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
    private val photoAdapter: PhotoAdapter by lazy {
        PhotoAdapter(mutableListOf())
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
        getLoadingObserver()
        getEmptyPhotosObserver()
    }

    private fun getEmptyPhotosObserver() {
        movieViewModel.emptyMoviePhotosLiveData.observe(viewLifecycleOwner) {
            binding.tvEmptyPhotos.visibility = if (it) View.VISIBLE else View.GONE
        }
    }

    private fun getLoadingObserver() {
        movieViewModel.loadingLiveData.observe(viewLifecycleOwner) {
            binding.layoutLoading.root.visibility = if (it) View.VISIBLE else View.GONE
        }
    }

    private fun getMoviePhotosObserver() {
        movieViewModel.moviePhotosLiveData.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    photoAdapter.updateList(it.data!!)
                    binding.groupPhotos.visibility = View.VISIBLE
                }

                Status.ERROR -> {
                    binding.groupPhotos.visibility = View.GONE
                    binding.tvEmptyPhotos.visibility = View.GONE
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
                groupCastMembers.visibility = View.VISIBLE
            }

            if (movie.genres.isNullOrEmpty()) {
                groupGenres.visibility = View.GONE
            } else {
                groupGenres.visibility = View.VISIBLE
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

            rvPhotos.isNestedScrollingEnabled = false
            rvPhotos.adapter = photoAdapter
        }
    }

}