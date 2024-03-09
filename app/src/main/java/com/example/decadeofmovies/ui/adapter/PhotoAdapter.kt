package com.example.decadeofmovies.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.decadeofmovies.databinding.ItemPhotoBinding
import com.example.decadeofmovies.model.FlickrPhoto

class PhotoAdapter(
    private val photosList: MutableList<FlickrPhoto>
) : RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        context = parent.context
        return PhotoViewHolder(
            ItemPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.bind(context, photosList[position])
    }

    override fun getItemCount(): Int {
        return photosList.size
    }

    fun updateList(data: List<FlickrPhoto>) {
        photosList.clear()
        photosList.addAll(data)
        this.notifyDataSetChanged()
    }

    class PhotoViewHolder(private val binding: ItemPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(context: Context, flickrPhoto: FlickrPhoto) {
            Glide.with(context)
                .load("https://farm${flickrPhoto.farm}.static.flickr.com/${flickrPhoto.server}/${flickrPhoto.id}_${flickrPhoto.secret}.jpg")
                .into(binding.ivPhoto)
        }
    }
}