package com.capstone.coursemate.dev

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
/*import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.capstone.coursemate.dev.data.SettingPreferences
import com.capstone.coursemate.dev.data.dataStore*/
import com.capstone.coursemate.dev.data.response.authentications.TokenManager
import com.capstone.coursemate.dev.databinding.ActivitySplashBinding
import com.capstone.coursemate.dev.ui.forrms.LoginFormActivity
/*import com.capstone.coursemate.dev.ui.settings.SettingsViewModel
import com.capstone.coursemate.dev.ui.settings.ViewModelFactory*/


@Suppress("DEPRECATION")
class SplashActivity : AppCompatActivity() {
    private lateinit var binding:ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)




        val tokenManager = TokenManager(this)
        val token = tokenManager.getToken()

        @Suppress("DEPRECATED")
        Handler().postDelayed(Runnable {
            val intent = if (token != null) {
                Intent(this, MainActivity::class.java)
            }else {
                Intent(this, LoginFormActivity::class.java)
            }

            startActivity(intent)
            finish()
        }, 3000)



    }
}