package com.example.decadeofmovies.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.decadeofmovies.R
import com.example.decadeofmovies.databinding.ItemMovieBinding
import com.example.decadeofmovies.model.Movie

class MovieAdapter(
    private val moviesList: MutableList<Movie>,
    private val movieListClickListener: MovieListClickListener
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        context = parent.context
        return MovieViewHolder(
            ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(context, moviesList[position], movieListClickListener)
    }

    override fun getItemCount(): Int {
        return moviesList.size
    }

    fun setMoviesList(data: List<Movie>) {
        moviesList.clear()
        moviesList.addAll(data)
        this.notifyDataSetChanged()
    }

    class MovieViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(context: Context, movie: Movie, movieListClickListener: MovieListClickListener) {
            binding.apply {
                tvTitle.text = movie.title ?: ""
                tvYear.text = if (movie.year == null) "" else context.getString(
                    R.string.year,
                    "${movie.year}"
                )
                tvRating.text = if (movie.rating == null) "" else context.getString(
                    R.string.rating,
                    "${movie.rating}"
                )
                cardMovie.setOnClickListener {
                    movieListClickListener.openMovieDetailS(movie)
                }
            }
        }
    }

    interface MovieListClickListener {
        fun openMovieDetailS(movie: Movie)
    }
}