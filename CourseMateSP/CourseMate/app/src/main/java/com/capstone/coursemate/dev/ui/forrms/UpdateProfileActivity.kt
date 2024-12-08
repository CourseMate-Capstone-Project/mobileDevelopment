package com.capstone.coursemate.dev.ui.forrms

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.capstone.coursemate.dev.data.response.authentications.TokenManager
import com.capstone.coursemate.dev.data.response.profiles.ProfileRepository
import com.capstone.coursemate.dev.data.response.profiles.ProfileViewModel
import com.capstone.coursemate.dev.data.response.profiles.ProfileViewModelFactory
import com.capstone.coursemate.dev.data.response.profiles.UpdateProfileRequest
import com.capstone.coursemate.dev.data.retrofit.cloudConfigs.ApiConfig2
import com.capstone.coursemate.dev.databinding.FormUpdateBinding
import java.io.ByteArrayOutputStream

class UpdateProfileActivity: AppCompatActivity(){
    private lateinit var binding: FormUpdateBinding

    private val profileViewModel: ProfileViewModel by viewModels {
        ProfileViewModelFactory(ProfileRepository(ApiConfig2.apiService))
    }

    private lateinit var tokenManager: TokenManager

    // Declare base64Image as a property
    //private var base64Image: String? = null
    private var imageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FormUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()



        profileViewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar2.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        tokenManager = TokenManager(this)

        binding.btnSelectImage.setOnClickListener {
            openImagePicker()
        }

        binding.btnSubmitUpdate.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            if (username.isEmpty() || imageUri == null) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else {
                val token = tokenManager.getToken()
                if (token != null) {
                    binding.progressBar2.visibility = View.VISIBLE
                    profileViewModel.updateProfile(token, username, imageUri!!, this)
                } else {
                    Toast.makeText(this, "Invalid token. Please login again.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        profileViewModel.updateResult.observe(this) { result ->
            binding.progressBar2.visibility = View.GONE
            result.onSuccess {
                Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
                setResult(Activity.RESULT_OK) // Notify SettingsFragment of the update
                finish()
            }
            result.onFailure {
                Log.e(this.toString(), "OBSERVEVIEWMODEL() error apa ini???? ${it.message}")
                Toast.makeText(this, "Error AAAupdating profile: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                imageUri = it
                binding.ivPreview.setImageURI(it) // Show preview

                // Log the URI and permissions
                Log.d("UpdateProfileActivity", "Selected URI: $it")
                val flags = contentResolver.openInputStream(it)?.available() ?: -1
                Log.d("UpdateProfileActivity", "File access available: $flags")
            }
        }

    private fun openImagePicker() {
        pickImageLauncher.launch("image/*")
    }



    private val readMediaPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                openImagePicker()
            } else {
                Toast.makeText(this, "Permission denied to read media files.", Toast.LENGTH_SHORT).show()
            }
        }

    private fun checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+
            if (checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                openImagePicker()
            } else {
                readMediaPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
            }
        } else { // For older Android versions
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                openImagePicker()
            } else {
                readMediaPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }


}

