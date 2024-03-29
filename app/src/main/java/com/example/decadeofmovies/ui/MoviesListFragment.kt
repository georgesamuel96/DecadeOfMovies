package com.example.decadeofmovies.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.example.decadeofmovies.R
import com.example.decadeofmovies.databinding.FragmentMoviesListBinding
import com.example.decadeofmovies.model.Movie
import com.example.decadeofmovies.network.EmptyMovieListStatus
import com.example.decadeofmovies.network.Status
import com.example.decadeofmovies.ui.adapter.MovieAdapter
import com.example.decadeofmovies.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoviesListFragment : Fragment(), MovieAdapter.MovieListClickListener {

    private var _binding: FragmentMoviesListBinding? = null
    private val movieViewModel: MovieViewModel by activityViewModels()

    private val binding get() = _binding!!
    private val moviesList = mutableListOf<Movie>()
    private val filteredMoviesList = mutableListOf<Movie>()
    private val movieAdapter: MovieAdapter by lazy {
        MovieAdapter(filteredMoviesList, this)
    }

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

        initialization()
        initializeViewModelObservers()
        listeners()

        return binding.root
    }

    private fun initialization() {
        binding.apply {
            rvMovie.adapter = movieAdapter
        }
    }

    private fun initializeViewModelObservers() {
        getMoviesListObserver()
        getEmptyMoviesListObserver()
        getLoadingObserver()
    }

    private fun getLoadingObserver() {
        movieViewModel.loadingLiveData.observe(viewLifecycleOwner) {
            binding.layoutLoading.root.visibility = if(it) View.VISIBLE else View.GONE
        }
    }

    private fun getEmptyMoviesListObserver() {
        movieViewModel.emptyMoviesListLiveData.observe(viewLifecycleOwner) {
            binding.apply {
                when(it) {
                    EmptyMovieListStatus.REQUEST -> {
                        tvEmptyList.text = getString(R.string.empty_movies_list_message)
                        tvEmptyList.visibility = View.VISIBLE
                        groupMoviesList.visibility = View.GONE
                    }
                    EmptyMovieListStatus.SEARCH -> {
                        tvEmptyList.text = getString(R.string.empty_search_movies_list_message)
                        tvEmptyList.visibility = View.VISIBLE
                        groupMoviesList.visibility = View.VISIBLE
                    }
                    EmptyMovieListStatus.NONE -> {
                        tvEmptyList.visibility = View.GONE
                        groupMoviesList.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun getMoviesListObserver() {
        movieViewModel.movieListLiveData.observe(viewLifecycleOwner) {
            when(it.status) {
                Status.SUCCESS -> {

                    //movies list used when user clear search, use the default list to view
                    //if the movies empty, that means is first time opened this view, and should save the default list
                    if(moviesList.isEmpty()) {
                        moviesList.addAll(it.data!!)
                    }

                    movieAdapter.setMoviesList(it.data!!)
                }
                Status.ERROR -> {

                }
            }
        }
    }

    private fun listeners() {
        binding.apply {

            btnClear.setOnClickListener {
                etSearch.setText("")
            }

            etSearch.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    val text: String = s?.toString()?: ""
                    movieViewModel.searchOnMovie(text.lowercase(), moviesList)
                }

            })
        }
    }

    override fun openMovieDetailS(movie: Movie) {
        movieViewModel.openMovieLiveData(movie)
        Navigation.findNavController(requireView())
            .navigate(R.id.action_moviesListFragment_to_movieDetailsFragment)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}