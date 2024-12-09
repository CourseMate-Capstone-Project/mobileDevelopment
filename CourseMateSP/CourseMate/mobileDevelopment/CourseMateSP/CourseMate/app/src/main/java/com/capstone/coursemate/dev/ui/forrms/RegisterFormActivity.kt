package com.capstone.coursemate.dev.ui.forrms

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.capstone.coursemate.dev.data.response.authentications.AuthRepository
import com.capstone.coursemate.dev.data.response.authentications.AuthViewModel
import com.capstone.coursemate.dev.data.response.authentications.AuthViewModelFactory
import com.capstone.coursemate.dev.data.response.authentications.TokenManager
import com.capstone.coursemate.dev.data.retrofit.cloudConfigs.ApiConfig2
import com.capstone.coursemate.dev.databinding.FormRegisterBinding

class RegisterFormActivity: AppCompatActivity() {
    private lateinit var binding: FormRegisterBinding
    private val authViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory(AuthRepository(ApiConfig2.apiService, TokenManager(this)))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FormRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.registerButton.setOnClickListener {
            val username = binding.usernameEditText.text.toString().trim()
            val fullName = binding.fullnameEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if (username.isEmpty() || fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else {
                authViewModel.register(username, fullName, email, password)
            }
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        authViewModel.authResult.observe(this) { result ->
            result.onSuccess {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                finish()
            }
            result.onFailure {
                Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }

        authViewModel.isLoading.observe(this) { isLoading ->
            binding.registerButton.isEnabled = !isLoading
        }
    }
}
