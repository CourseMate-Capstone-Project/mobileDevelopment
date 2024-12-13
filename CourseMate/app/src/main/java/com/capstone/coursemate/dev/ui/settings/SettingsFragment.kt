package com.capstone.coursemate.dev.ui.settings


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.capstone.coursemate.dev.data.SettingPreferences
import com.capstone.coursemate.dev.data.dataStore
import com.capstone.coursemate.dev.data.response.authentications.TokenManager
import com.capstone.coursemate.dev.data.response.profiles.ProfileRepository
import com.capstone.coursemate.dev.data.response.profiles.ProfileViewModel
import com.capstone.coursemate.dev.data.response.profiles.ProfileViewModelFactory
import com.capstone.coursemate.dev.data.retrofit.cloudConfigs.ApiConfig2
import com.capstone.coursemate.dev.databinding.FragmentSettingsBinding
import com.capstone.coursemate.dev.ui.forrms.LoginFormActivity
import com.capstone.coursemate.dev.ui.forrms.UpdateProfileActivity

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val profileViewModel: ProfileViewModel by viewModels {
        ProfileViewModelFactory(ProfileRepository(ApiConfig2.apiService))
    }

    private lateinit var viewModel: SettingsViewModel

    private val updateProfileLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                refreshProfileData() // Refresh profile when returning
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root


        val tokenManager = TokenManager(requireContext())
        val token = tokenManager.getToken()
        //debug
        Log.d("SettingsFragment", "TOKEN Retrieved Token: $token")

        if (token != null) {
            profileViewModel.fetchProfile(token)
        }

        setupObservers()
            //LOGOUT
        val btnLogout = binding.btnLogout
        btnLogout.setOnClickListener{
            logout()
        }
        //      UPDATE PROFILE
        binding.btnUpdateProfile.setOnClickListener {
            val intent = Intent(requireContext(), UpdateProfileActivity::class.java)
            //startActivity(intent)
            updateProfileLauncher.launch(intent)
        }

        //      UNTUK APP SETTINGS

        val switchTheme = binding.switchTheme
        val viewModelFactory = ViewModelFactory(SettingPreferences.getInstance(requireActivity().applicationContext.dataStore))
        viewModel = ViewModelProvider(this, viewModelFactory).get(SettingsViewModel::class.java)

        // Observe theme livedata settings
        viewModel.getThemeSettings().observe(viewLifecycleOwner) { isDarkModeActive ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchTheme.isChecked = false
            }
        }

        // Set onClickListener for switch
        switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            viewModel.saveThemeSetting(isChecked)
        }

        return root
    }

    private fun logout() {
        AlertDialog.Builder(requireContext())
            .setTitle("Logout")
            .setMessage("Are you sure you want to log out?")
            .setPositiveButton("Yes") { _, _ ->
                // Clear the token
                val tokenManager = TokenManager(requireContext())
                tokenManager.clearToken()

                // Clear recommendation data from SharedPreferences
                val sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                sharedPreferences.edit().remove("recommended_courses").apply()

                // Navigate to LoginFormActivity
                val intent = Intent(requireContext(), LoginFormActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun setupObservers() {
        profileViewModel.profileData.observe(viewLifecycleOwner) { profile ->
            Log.e("SettingsFragment", " BINDING Profile Observed: $profile")
            binding.tvpUsername.text = profile.username
            binding.tvpFullname.text = profile.full_name
            binding.tvpEmail.text = profile.email

            Glide.with(this)
                .load(profile.profile_picture)
                .transform(CenterCrop())
                .into(binding.ivpProfilePicture)
        }

/*        profileViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }*/

        profileViewModel.error.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                Log.e("SettingsFragment", " ERROR ERROR Error Observed: $error")
            // Handle error (e.g., show a Toast or SnackBar)
            }
        }
    }


    private fun refreshProfileData() {
        val tokenManager = TokenManager(requireContext())
        val token = tokenManager.getToken()
        if (token != null) {
            Log.d("SettingsFragment", "refreshProfileData: Refreshing profile with token: $token")
            profileViewModel.fetchProfile(token)
        } else {
            Toast.makeText(requireContext(), "Token is missing. Please login again.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}