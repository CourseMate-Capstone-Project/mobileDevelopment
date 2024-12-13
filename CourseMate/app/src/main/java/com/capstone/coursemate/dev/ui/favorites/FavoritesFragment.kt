package com.capstone.coursemate.dev.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.coursemate.dev.data.database.CourseDatabase
import com.capstone.coursemate.dev.data.database.CourseRepository
import com.capstone.coursemate.dev.data.response.authentications.TokenManager
import com.capstone.coursemate.dev.data.response.favourites.FavoritesRepository
import com.capstone.coursemate.dev.data.response.favourites.FavoritesViewModelFactory
import com.capstone.coursemate.dev.data.response.recommendationAPI.Course
import com.capstone.coursemate.dev.data.retrofit.cloudConfigs.ApiConfig2
import com.capstone.coursemate.dev.databinding.FragmentFavoritesBinding

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val favoritesViewModel: FavoritesViewModel by lazy {
        val favoritesRepository = FavoritesRepository(ApiConfig2.apiService)
        val courseRepository = CourseRepository(CourseDatabase.getDatabase(requireContext()).courseDao())
        val factory = FavoritesViewModelFactory(favoritesRepository, courseRepository)
        ViewModelProvider(this, factory)[FavoritesViewModel::class.java]
    }

    private lateinit var favoritesAdapter: FavouritesAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupRecyclerView()

        val tokenManager = TokenManager(requireContext())
        val token = tokenManager.getToken()

        if (token != null) {
            favoritesViewModel.fetchFavorites(token)
        } else {
            Toast.makeText(requireContext(), "Token not found. Please log in.", Toast.LENGTH_SHORT).show()
        }

        observeViewModel()


        return root
    }
    private fun setupRecyclerView() {
        favoritesAdapter = FavouritesAdapter { course ->
            handleFavoriteToggle(course) // Handle toggle logic locally
        }
        //favoritesAdapter = FavouritesAdapter()
        binding.rvfList.adapter = favoritesAdapter
        binding.rvfList.layoutManager = LinearLayoutManager(requireContext())
    }
    private fun handleFavoriteToggle(course: Course) {
        favoritesViewModel.removeFromFavorites(course)
        Toast.makeText(requireContext(), "Removed from favorites", Toast.LENGTH_SHORT).show()
    }
    private fun observeViewModel() {
        favoritesViewModel.favorites.observe(viewLifecycleOwner) { favoriteCourses ->
            //favoritesAdapter.submitList(favoriteCourses)      //preserved for future api implementation
        }

        favoritesViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar3.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        favoritesViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
        favoritesViewModel.allFavorites.observe(viewLifecycleOwner) { favoriteCourses ->
            favoritesAdapter.submitList(favoriteCourses)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}