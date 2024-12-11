package com.capstone.coursemate.dev.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.coursemate.dev.data.response.authentications.TokenManager
import com.capstone.coursemate.dev.data.response.favourites.FavoritesRepository
import com.capstone.coursemate.dev.data.response.favourites.FavoritesViewModelFactory
import com.capstone.coursemate.dev.data.retrofit.cloudConfigs.ApiConfig2
import com.capstone.coursemate.dev.databinding.FragmentFavoritesBinding

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val favoritesViewModel: FavoritesViewModel by viewModels {
        FavoritesViewModelFactory(FavoritesRepository(ApiConfig2.apiService))
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
        favoritesAdapter = FavouritesAdapter()
        binding.rvfList.adapter = favoritesAdapter
        binding.rvfList.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observeViewModel() {
        favoritesViewModel.favorites.observe(viewLifecycleOwner) { favoriteCourses ->
            favoritesAdapter.submitList(favoriteCourses)
        }

        favoritesViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar3.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        favoritesViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}