package com.capstone.coursemate.dev.ui.favorites

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.capstone.coursemate.dev.R
import com.capstone.coursemate.dev.data.response.recommendationAPI.Course
import com.capstone.coursemate.dev.databinding.ItemRowFavouriteBinding

class FavouritesAdapter(private val onFavoriteToggle: (Course) -> Unit) : ListAdapter<Course, FavouritesAdapter.FavoritesViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        val binding = ItemRowFavouriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoritesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        val course = getItem(position)
        holder.bind(course)
    }

    inner class FavoritesViewHolder(private val binding: ItemRowFavouriteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(course: Course) {
            with(binding) {
                tvCategory.text = course.predicted_category
                tvShortIntro.text = course.short_intro
                tvCreatedAt.text = course.created_at
                tvTitle.text = course.title

                fabButton.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(course.url))
                    it.context.startActivity(intent)
                }

                // Handle favorite toggle
                fabFavoriteToggle.setOnClickListener {
                    //Toast.makeText(it.context, "Favorite clicked for ${course.title}", Toast.LENGTH_SHORT).show()
                    course.isFavorite = !course.isFavorite
                    onFavoriteToggle(course)
                    fabFavoriteToggle.setImageResource(
                        if (course.isFavorite) R.drawable.round_bookmark_added_24 else R.drawable.round_bookmark_border_24
                    )
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Course>() {
            override fun areItemsTheSame(oldItem: Course, newItem: Course): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Course, newItem: Course): Boolean {
                return oldItem == newItem
            }
        }
    }
}