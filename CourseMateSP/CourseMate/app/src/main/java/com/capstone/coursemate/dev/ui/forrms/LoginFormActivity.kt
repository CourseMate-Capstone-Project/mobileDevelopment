package com.capstone.coursemate.dev.ui.forrms

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.capstone.coursemate.dev.MainActivity
import com.capstone.coursemate.dev.data.response.authentications.AuthRepository
import com.capstone.coursemate.dev.data.response.authentications.AuthViewModel
import com.capstone.coursemate.dev.data.response.authentications.AuthViewModelFactory
import com.capstone.coursemate.dev.data.response.authentications.TokenManager
import com.capstone.coursemate.dev.data.retrofit.cloudConfigs.ApiConfig2
import com.capstone.coursemate.dev.databinding.FormLoginBinding

class LoginFormActivity :AppCompatActivity(){
    private lateinit var binding: FormLoginBinding
    private val authViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory(AuthRepository(ApiConfig2.apiService, TokenManager(this)))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FormLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.loginButton.setOnClickListener {
            val username = binding.usernameEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else {
                authViewModel.login(username, password)
            }
        }

        binding.registerText.setOnClickListener {
            startActivity(Intent(this, RegisterFormActivity::class.java))
        }

        observeViewModel()
    }

    private fun observeViewModel() {

        authViewModel.authResult.observe(this) { result ->
            result.onSuccess {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            result.onFailure {
                Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }

        authViewModel.isLoading.observe(this) { isLoading ->
            binding.loginButton.isEnabled = !isLoading
        }
    }
}