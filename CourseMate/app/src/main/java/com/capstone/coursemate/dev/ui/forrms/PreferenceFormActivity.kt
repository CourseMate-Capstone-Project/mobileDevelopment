package com.capstone.coursemate.dev.ui.forrms

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.capstone.coursemate.dev.R
import com.capstone.coursemate.dev.data.response.recommendationAPI.Course
import com.capstone.coursemate.dev.data.response.recommendationAPI.RecommendationRequest
import com.capstone.coursemate.dev.databinding.FormPreferenceBinding
import com.capstone.coursemate.dev.ui.home.HomeViewModel
import com.google.gson.Gson

class PreferenceFormActivity : AppCompatActivity() {

    private lateinit var binding: FormPreferenceBinding
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = FormPreferenceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()


        // Use ViewModelProvider to get the shared HomeViewModel instance
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)


        val interestSpinner : Spinner = binding.interestSpinner
        val courseTypeSpinner : Spinner = binding.courseTypeSpinner
        val durationSSpinner : Spinner = binding.durationSpinner
        val savePreferenceButton = binding.savePreferenceButton


        setupSpinner(interestSpinner, R.array.interest_options)
        setupSpinner(courseTypeSpinner, R.array.course_type_options)
        setupSpinner(durationSSpinner, R.array.duration_options)




        savePreferenceButton.setOnClickListener {
            val interest = interestSpinner.selectedItem.toString()
            val courseType = courseTypeSpinner.selectedItem.toString()
            val duration = durationSSpinner.selectedItem.toString()

            // Validate that the user has selected a valid option, not the default
            if (interest == "Select Your Interest" || courseType == "Select Course Type" || duration == "Select Max Duration (In Weeks)") {
                Toast.makeText(baseContext, "Please select all options correctly.", Toast.LENGTH_SHORT).show()
            } else {
                fetchRecommendations(RecommendationRequest(interest, courseType, duration))
            }
        }


    }

    private fun setupSpinner(spinner: Spinner, arrayResId: Int) {
        ArrayAdapter.createFromResource(
            baseContext,
            arrayResId,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
    }

    private fun fetchRecommendations(request: RecommendationRequest) {
        binding.progressBar.visibility = View.VISIBLE
        homeViewModel.getRecommendations(request).observe(this) { result ->
            binding.progressBar.visibility = View.GONE
            if (result.isSuccess) {
                val response = result.getOrNull()
                if (response != null) {
                    val courses = response.recommended_courses
                    Log.d("PreferenceFormActivity", "Fetched courses: $courses")

                    // Save courses
                    saveCoursesToSharedPreferences(courses)
                    savePredictedCategoryToSharedPreferences(response.predicted_category)

                    // Set a flag to indicate that the preference form has been filled
                    setPreferenceFormFilledFlag()
                    // Set result to indicate success
                    setResult(Activity.RESULT_OK)
                    finish()
                } else {
                    Toast.makeText(baseContext, "There is an error while fetching data.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(baseContext, "There is an error while calling the API. Are you disconnected from internet?", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveCoursesToSharedPreferences(courses: List<Course>) {
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(courses)
        Log.e("PreferenceFormActivity", "Saving courses to SharedPreferences: $json")
        editor.putString("recommended_courses", json)
        editor.apply()
    }

    private fun setPreferenceFormFilledFlag() {
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("isFormFilled", true)
        editor.commit()  // Use .commit() to make sure it is written immediately
    }

    private fun savePredictedCategoryToSharedPreferences(predictedCategory: String) {
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("predicted_category", predictedCategory)
        editor.commit()  // Use .commit() to make sure it is written immediately
    }
}