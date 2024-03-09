package com.example.decadeofmovies.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.decadeofmovies.R
import com.example.decadeofmovies.databinding.ItemMovieBinding
import com.example.decadeofmovies.databinding.ItemYearBinding
import com.example.decadeofmovies.model.Movie

class MovieAdapter(
    private val moviesList: MutableList<Movie>,
    private val movieListClickListener: MovieListClickListener
) : RecyclerView.Adapter<BaseViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        context = parent.context

        return when(viewType) {
            0 -> {
                YearViewHolder(
                    ItemYearBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                )
            }
            else -> {
                MovieViewHolder(
                    ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                )
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (moviesList[position].rating) {
            -1 -> 0
            else -> 1
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        when(holder) {
            is YearViewHolder -> {
                holder.bind(AdapterMovieTypes.ItemYear(moviesList[position].year?: 0))
            }
            else -> {
                holder.bind(AdapterMovieTypes.ItemMovie(context, moviesList[position], movieListClickListener))
            }
        }
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
        BaseViewHolder(binding.root) {

        override fun bind(item: AdapterMovieTypes) {
            if(item is AdapterMovieTypes.ItemMovie)
            binding.apply {
                tvTitle.text = item.movie.title ?: ""
                tvYear.text = if (item.movie.year == null) "" else item.context.getString(
                    R.string.year,
                    "${item.movie.year}"
                )
                tvRating.text = if (item.movie.rating == null) "" else item.context.getString(
                    R.string.rating,
                    "${item.movie.rating}"
                )
                cardMovie.setOnClickListener {
                    item.movieListClickListener.openMovieDetailS(item.movie)
                }
            }
        }
    }

    class YearViewHolder(private val binding: ItemYearBinding) :
        BaseViewHolder(binding.root) {

        override fun bind(item: AdapterMovieTypes) {
            if(item is AdapterMovieTypes.ItemYear)
                binding.apply {
                    tvYear.text = "${item.year}"
                }
        }
    }

    interface MovieListClickListener {
        fun openMovieDetailS(movie: Movie)
    }
}