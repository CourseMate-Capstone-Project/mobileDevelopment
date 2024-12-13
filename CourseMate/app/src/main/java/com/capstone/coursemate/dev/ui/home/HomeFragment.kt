package com.capstone.coursemate.dev.ui.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.coursemate.dev.data.database.CourseDatabase
import com.capstone.coursemate.dev.data.database.CourseRepository
import com.capstone.coursemate.dev.data.response.authentications.TokenManager
import com.capstone.coursemate.dev.data.response.favourites.FavoritesRepository
import com.capstone.coursemate.dev.data.response.favourites.FavoritesViewModelFactory
import com.capstone.coursemate.dev.data.response.recommendationAPI.Course
import com.capstone.coursemate.dev.data.retrofit.cloudConfigs.ApiConfig2
import com.capstone.coursemate.dev.databinding.FragmentHomeBinding
import com.capstone.coursemate.dev.ui.favorites.FavoritesViewModel
import com.capstone.coursemate.dev.ui.forrms.PreferenceFormActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var recommendationsAdapter: RecommendationsAdapter
    private val homeViewModel: HomeViewModel by lazy {
        ViewModelProvider(requireActivity())[HomeViewModel::class.java]
    }
    private val favoritesViewModel: FavoritesViewModel by lazy {
        val favoritesRepository = FavoritesRepository(ApiConfig2.apiService)
        val courseRepository = CourseRepository(CourseDatabase.getDatabase(requireContext()).courseDao())
        val factory = FavoritesViewModelFactory(favoritesRepository, courseRepository)
        ViewModelProvider(this, factory)[FavoritesViewModel::class.java]
    }
    /*private val homeViewModel: HomeViewModel by lazy {
        val favoritesRepository = FavoritesRepository(ApiConfig2.apiService)
        ViewModelProvider(this, HomeViewModelFactory(favoritesRepository))[HomeViewModel::class.java]
    }*/
    //private val homeViewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Set up RecyclerView and Adapter              THIS
/*        recommendationsAdapter = RecommendationsAdapter { course ->
            toggleFavorite(course) // Handle the favorite toggle here
        }*/


        val tokenManager = TokenManager(requireContext())
        val token = tokenManager.getToken() ?: ""
        val repository = FavoritesRepository(ApiConfig2.apiService)
        //recommendationsAdapter.setDependencies(token, repository)
        //recommendationsAdapter = RecommendationsAdapter()
        recommendationsAdapter = RecommendationsAdapter { course ->
            favoritesViewModel.toggleFavorite(course)
        }
        binding.rvRecomm.adapter = recommendationsAdapter
        binding.rvRecomm.layoutManager = LinearLayoutManager(context)
        binding.rvRecomm.isNestedScrollingEnabled = false

        // Load courses from SharedPreferences and set the data in the adapter
        val courses = loadCoursesFromSharedPreferences()
        if (courses != null && courses.isNotEmpty()) {
            Log.d("HomeFragment", "Loaded courses from SharedPreferences: $courses")
            recommendationsAdapter.submitList(courses)
        } else {
            Log.d("HomeFragment", "No courses found in SharedPreferences")
        }

        // Observe errors
        homeViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }

        //onclick card ke freference form
        val prefcard = binding.cardView2
        prefcard.setOnClickListener{
            val intent = Intent(context, PreferenceFormActivity::class.java)
            @Suppress("DEPRECATION")
            startActivityForResult(intent, REQUEST_CODE)
        }

        return root

    }

    private fun handleFavoriteToggle(course: Course) {
        if (course.isFavorite) {
            favoritesViewModel.removeFromFavorites(course)
            Toast.makeText(requireContext(), "${course.title} removed from favorites", Toast.LENGTH_SHORT).show()
        } else {
            favoritesViewModel.addToFavorites(course)
            Toast.makeText(requireContext(), "${course.title} added to favorites", Toast.LENGTH_SHORT).show()
        }
    }

    private fun toggleFavorite(course: Course) {
        Log.e("HomeFragment", "Favorite toggle triggered for course: ${course.id}")
        val tokenManager = TokenManager(requireContext())
        val token = tokenManager.getToken()
        if (token.isNullOrEmpty()) {
            Toast.makeText(
                requireContext(),
                "Token not available. Please log in.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Load courses from SharedPreferences and set the data in the adapter
            val courses = loadCoursesFromSharedPreferences()
            if (courses != null && courses.isNotEmpty()) {
                Log.d("HomeFragment", "Loaded courses from SharedPreferences in onActivityResult: $courses")
                recommendationsAdapter.submitList(courses)
                recommendationsAdapter.notifyDataSetChanged()
            } else {
                Log.d("HomeFragment", "No courses found in SharedPreferences in onActivityResult")
            }
        }
    }
    private fun loadAndDisplayCourses() {
        val courses = loadCoursesFromSharedPreferences()
        if (courses != null && courses.isNotEmpty()) {
            Log.d("HomeFragment", "Loaded courses from SharedPreferences: $courses")
            recommendationsAdapter.submitList(courses)
            recommendationsAdapter.notifyDataSetChanged()
        } else {
            Log.d("HomeFragment", "No courses found in SharedPreferences")
        }
    }
    companion object {
        private const val REQUEST_CODE = 100  // Request code for starting PreferenceFormActivity
    }

    private fun loadCoursesFromSharedPreferences(): List<Course>? {
        val sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("recommended_courses", null)
        Log.d("HomeFragment", "Loading courses JSON from SharedPreferences: $json")
        val type = object : TypeToken<List<Course>>() {}.type
        return if (json != null) {
            gson.fromJson(json, type)
        } else {
            null
        }
    }

    private fun updateCardTextBasedOnFormStatus() {
        val sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val isFormFilled = sharedPreferences.getBoolean("isFormFilled", false)

        if (isFormFilled) {
            // Update the text to indicate the form has been filled
            binding.textView19.text = "Click here to redo the Preference Form."
        } else {
            // Default text when the form has not been filled yet
            binding.textView19.text = "You do not have any preference data yet. Tap here to fill the form."
        }
    }
    private fun updatePredictedCategory() {
        val sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val predictedCategory = sharedPreferences.getString("predicted_category", "Not Available")

        // Update the TextView with the predicted category
        binding.tvPredictedCategory.text = "Predicted Category: $predictedCategory"
    }

    override fun onResume() {
        super.onResume()
        loadAndDisplayCourses()  // Also load data in onResume()
        updateCardTextBasedOnFormStatus()  // Update card text when the fragment resumes
        updatePredictedCategory()  // Update the predicted category when the fragment resumes
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}