package com.capstone.coursemate.dev.ui.home


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.coursemate.dev.data.response.authentications.TokenManager
import com.capstone.coursemate.dev.data.response.favourites.FavoriteRequest
import com.capstone.coursemate.dev.data.response.favourites.FavoritesRepository
import com.capstone.coursemate.dev.data.response.recommendationAPI.Course
import com.capstone.coursemate.dev.data.response.recommendationAPI.RecommendationRequest
import com.capstone.coursemate.dev.data.response.recommendationAPI.RecommendationResponse
import com.capstone.coursemate.dev.data.retrofit.recommConfigs.RecommendationRepository
import kotlinx.coroutines.launch




class HomeViewModel : ViewModel() {


    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text



    // Repository instance to call API
    private val repository = RecommendationRepository()

    // MutableLiveData to store the list of recommended courses
    private val _recommendedCourses = MutableLiveData<List<Course>>()
    val recommendedCourses: LiveData<List<Course>> = _recommendedCourses

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage




    // Fetch recommendations using the repository
    fun getRecommendations(request: RecommendationRequest): LiveData<Result<RecommendationResponse>> {
        return repository.getRecommendations(request)
    }

// from first test,
// Function to set/update the recommended courses
    fun setRecommendedCoursesFromResponse(response: RecommendationResponse) {
        // Trigger change explicitly by setting to null first (forcing the observer to update)
        _recommendedCourses.value = emptyList() // Or `_recommendedCourses.value = null`
        _recommendedCourses.value = response.recommended_courses
    }



    // Function: Toggle favorite for a specific course
    fun toggleFavorite(card: Course, tokenManager: TokenManager, favouritesRepository: FavoritesRepository) {
        viewModelScope.launch {
            try {
                // Get the token
                val token = tokenManager.getToken()
                if (token.isNullOrEmpty()) {
                    _errorMessage.value = "Token not found. Please log in."
                    return@launch
                }

                // Update the local favorite status
                val updatedCard = card.copy(isFavorite = !card.isFavorite)
                favouritesRepository.toggleFavorite("Bearer $token", updatedCard.toFavoriteRequest() ) // API sync

                // Update the local LiveData list
                val updatedList = _recommendedCourses.value?.map {
                    if (it.id == card.id) updatedCard else it
                }
                _recommendedCourses.value = updatedList
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }

    // Helper Function: Convert Course to FavoriteRequest for API
    private fun Course.toFavoriteRequest(): FavoriteRequest {
        return FavoriteRequest(
            title = this.title,
            short_intro = this.short_intro,
            url = this.url,
            predicted_category = this.predicted_category!!
        )
    }

}



/*
class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val courseRepository: CourseRepository

    private val _recommendedCourses = MutableLiveData<List<Course>>()
    val recommendedCourses: LiveData<List<Course>>

    init {
        val courseDao = CourseDatabase.getDatabase(application).courseDao()
        courseRepository = CourseRepository(courseDao)
        recommendedCourses = courseRepository.allCourses // Existing name mapped
    }

    fun insertCourses(courses: List<Course>) = viewModelScope.launch {
        courseRepository.insertAll(courses)
    }

    fun clearCourses() = viewModelScope.launch {
        courseRepository.deleteAll()
    }

    // Function to fetch recommendations using the repository
    fun getRecommendations(request: RecommendationRequest): LiveData<Result<RecommendationResponse>> {
        return Repository.getRecommendations(request)
    }
}*/
