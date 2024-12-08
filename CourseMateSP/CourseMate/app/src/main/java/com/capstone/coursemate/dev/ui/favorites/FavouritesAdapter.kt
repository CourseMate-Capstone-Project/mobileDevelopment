package com.capstone.coursemate.dev.ui.favorites

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.capstone.coursemate.dev.R
import com.capstone.coursemate.dev.data.response.recommendationAPI.Course
import com.capstone.coursemate.dev.databinding.ItemRowFavouriteBinding

class FavouritesAdapter : ListAdapter<Course, FavouritesAdapter.FavoritesViewHolder>(DIFF_CALLBACK) {

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
                // Bind course details to UI
                tvCategory.text = course.category
                tvInterest.text = course.interest

                tvTitle.text = course.title


                // Handle button click for course details
                btnViewDetails.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(course.url))
                    it.context.startActivity(intent)
                }

                // Handle favorite toggle
                ivFavoriteToggle.setImageResource(
                    if (course.isFavorite) R.drawable.bookmark_added_24 else R.drawable.bookmark_border_24
                )
                ivFavoriteToggle.setOnClickListener {
                    // Optional: Handle toggle locally if required
                    Toast.makeText(it.context, "Favorite clicked for ${course.title}", Toast.LENGTH_SHORT).show()
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