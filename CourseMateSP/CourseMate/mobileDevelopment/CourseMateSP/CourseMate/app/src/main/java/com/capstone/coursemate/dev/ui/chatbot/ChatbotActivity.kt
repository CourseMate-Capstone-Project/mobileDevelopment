package com.capstone.coursemate.dev.ui.chatbot

import android.os.Bundle
import android.os.PersistableBundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.capstone.coursemate.dev.databinding.AactivityChatbotBinding

class ChatbotActivity : AppCompatActivity() {
    private lateinit var binding: AactivityChatbotBinding
    private lateinit var webView: WebView
    private val viewModel: WebViewViewModel by viewModels() // Use ViewModel for state management
    private val initialUrl = "https://coursemate-chatbot-866939629489.asia-southeast2.run.app/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AactivityChatbotBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide() // Hide the action bar

        webView = binding.wvChatbot
        setupWebView()

        // Restore WebView state from ViewModel or load initial URL
        if (viewModel.webViewState != null) {
            webView.restoreState(viewModel.webViewState!!)
        } else {
            webView.loadUrl(initialUrl)
        }
    }

    private fun setupWebView() {
        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            useWideViewPort = true
            loadWithOverviewMode = true
            builtInZoomControls = true
            displayZoomControls = false
        }

        // Ensure WebView content loads within the app
        webView.webViewClient = WebViewClient()
    }

    override fun onPause() {
        super.onPause()
        // Save WebView state to ViewModel
        viewModel.webViewState = Bundle().also {
            webView.saveState(it)
        }
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack() // Navigate back in WebView history
        } else {
            super.onBackPressed() // Exit the activity
        }
    }
}
