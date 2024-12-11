package com.capstone.coursemate.dev.ui.home

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.capstone.coursemate.dev.R
import com.capstone.coursemate.dev.data.response.favourites.FavoriteRequest
import com.capstone.coursemate.dev.data.response.favourites.FavoritesRepository
import com.capstone.coursemate.dev.data.response.recommendationAPI.Course
import com.capstone.coursemate.dev.databinding.ItemRowClassicBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecommendationsAdapter: ListAdapter<Course, RecommendationsAdapter.TestEndedViewHolder>(DIFF_CALLBACK) {

    /*private var token: String? = null
    private var favoritesRepository: FavoritesRepository? = null

    fun setDependencies(token: String, repository: FavoritesRepository) {
        this.token = token
        this.favoritesRepository = repository
    }*/


    // on createnya
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestEndedViewHolder {
        val binding = ItemRowClassicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TestEndedViewHolder(binding)
    }

    // binding holder
    override fun onBindViewHolder(holder: TestEndedViewHolder, position: Int) {
        val card = getItem(position)
        holder.bind(card)

        //setonclicklistener
        /*holder.itemView.setOnClickListener() {
            //onItemClickListener(card) // Pass the clicked event item
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_ID, card.id.toString())
            holder.itemView.context.startActivity(intent)

        }*/

    }



    // holdernya
    class TestEndedViewHolder(private val binding: ItemRowClassicBinding): RecyclerView.ViewHolder(binding.root) {


        fun bind(card: Course){
            with(binding){
                //binding name
                itemName.text = card.title
                textView2.text = card.short_intro
                fabButton.setOnClickListener{
                    val eventLink = card.url // Assuming 'link' is the property in your EventDetail model
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(eventLink)
                    it.context.startActivity(intent)
                }
                // Handle favorite toggle
                fabFavoriteToggle.setImageResource(
                    if (card.isFavorite) R.drawable.bookmark_added_24 else R.drawable.bookmark_border_24
                )
                fabFavoriteToggle.setOnClickListener {
                    // Optional: Handle toggle locally if required
                    Toast.makeText(it.context, "Favorite clicked for ${card.title}", Toast.LENGTH_SHORT).show()

                }
/*
                ivFavoriteToggle.setOnClickListener {
                    val token: String? = null
                    val favoritesRepository: FavoritesRepository? = null


                    card.isFavorite = !card.isFavorite
                    notifyItemChanged(adapterPosition)

                    // Perform the favorite toggle API call
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            favoritesRepository?.toggleFavorite(token ?: "", FavoriteRequest(card.id.toString()))
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                // Revert the favorite toggle on failure
                                card.isFavorite = !card.isFavorite
                                notifyItemChanged(adapterPosition)
                                Toast.makeText(
                                    it.context,
                                    "Failed to update favorite status.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }*/



            }

        }




    }
    //companion obj untuk... ya gitulah
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Course>() {
            override fun areItemsTheSame(oldItem: Course, newItem:  Course): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Course, newItem: Course): Boolean {
                return oldItem == newItem
            }
        }
    }
}