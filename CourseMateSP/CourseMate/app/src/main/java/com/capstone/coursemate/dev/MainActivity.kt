package com.capstone.coursemate.dev

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.capstone.coursemate.dev.data.SettingPreferences
import com.capstone.coursemate.dev.data.dataStore
import com.capstone.coursemate.dev.databinding.ActivityMainBinding
import com.capstone.coursemate.dev.ui.chatbot.ChatbotActivity
import com.capstone.coursemate.dev.ui.settings.SettingsViewModel
import com.capstone.coursemate.dev.ui.settings.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar!!.setIcon(R.mipmap.ic_launcher_round)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


        // settings viewmodel UNTUK DARK MODE
        val viewModelFactory = ViewModelFactory(SettingPreferences.getInstance(applicationContext.dataStore))
        val viewModel = ViewModelProvider(this, viewModelFactory).get(SettingsViewModel::class.java)

        // Observe theme livedata settings
        viewModel.getThemeSettings().observe(this) { isDarkModeActive ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }

        }

        binding.extendedFab.setOnClickListener{
            val intent = Intent(this, ChatbotActivity::class.java)
            //intent.putExtra("URL", "https://www.google.com") // Replace with your chatbot URL
            startActivity(intent)
        }



    }


}