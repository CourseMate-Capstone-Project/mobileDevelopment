package com.capstone.coursemate.dev.ui.forrms

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.capstone.coursemate.dev.data.response.authentications.ResetPasswordRepository
import com.capstone.coursemate.dev.data.response.authentications.ResetPasswordViewModel
import com.capstone.coursemate.dev.data.response.authentications.ResetPasswordViewModelFactory
import com.capstone.coursemate.dev.data.retrofit.cloudConfigs.ApiConfig2
import com.capstone.coursemate.dev.databinding.FormResetPasswordBinding

class ResetPasswordFormActivity : AppCompatActivity() {

    private lateinit var binding: FormResetPasswordBinding
    private val resetPasswordViewModel: ResetPasswordViewModel by viewModels {
        ResetPasswordViewModelFactory(ResetPasswordRepository(ApiConfig2.apiService))
    }

    private var isOtpStage = false // Flag to track which stage of the flow we're in
    private var emailValue: String = "" // To preserve email value between requests

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FormResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setupViewVisibility()
        setupClickListeners()
        observeViewModel()
    }

    private fun setupViewVisibility() {
        if (isOtpStage) {
            // Show OTP and new password fields
            binding.prEmailLabel.visibility = View.GONE
            binding.prEmailEditText.visibility = View.GONE
            binding.prOtpLable.visibility = View.VISIBLE
            binding.prOtpEditText.visibility = View.VISIBLE
            binding.prNewPasswordLabel.visibility = View.VISIBLE
            binding.prNewPasswordEditText.visibility = View.VISIBLE
        } else {
            // Show email input
            binding.prEmailLabel.visibility = View.VISIBLE
            binding.prEmailEditText.visibility = View.VISIBLE
            binding.prOtpLable.visibility = View.GONE
            binding.prOtpEditText.visibility = View.GONE
            binding.prNewPasswordLabel.visibility = View.GONE
            binding.prNewPasswordEditText.visibility = View.GONE
        }
    }

    private fun setupClickListeners() {
        binding.btnConfirm.setOnClickListener {
            if (!isOtpStage) {
                // Email request stage
                val email = binding.prEmailEditText.text.toString().trim()
                if (email.isEmpty()) {
                    Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
                } else {
                    emailValue = email // Preserve email for the next stage
                    resetPasswordViewModel.requestPasswordReset(email)
                }
            } else {
                // OTP + new password stage
                val otp = binding.prOtpEditText.text.toString().trim()
                val newPassword = binding.prNewPasswordEditText.text.toString().trim()
                if (otp.isEmpty() || newPassword.isEmpty()) {
                    Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                } else {
                    resetPasswordViewModel.resetPassword(emailValue, otp, newPassword)
                }
            }
        }
    }

    private fun observeViewModel() {
        resetPasswordViewModel.isLoading.observe(this) { isLoading ->
            binding.btnConfirm.isEnabled = !isLoading
        }

        resetPasswordViewModel.apiResponse.observe(this) { response ->
            response?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                if (!isOtpStage) {
                    // Move to the OTP + new password stage
                    isOtpStage = true
                    setupViewVisibility()
                } else {
                    // Finish activity after successful reset
                    finish()
                }
            }
        }

        resetPasswordViewModel.error.observe(this) { error ->
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
