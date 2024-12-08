package com.capstone.coursemate.dev.ui.chatbot

import android.os.Bundle
import android.os.PersistableBundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.capstone.coursemate.dev.databinding.AactivityChatbotBinding

class ChatbotActivity :AppCompatActivity(){
    private lateinit var binding:AactivityChatbotBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AactivityChatbotBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val webView:WebView = binding.wvChatbot
        val url = "https://coursemate-chatbot-866939629489.asia-southeast2.run.app/"
        //val url = intent.getStringExtra("URL")

        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true // Enable DOM storage
            useWideViewPort = true // Adjust layout to fit screen
            loadWithOverviewMode = true // Load the webpage fully zoomed out
            builtInZoomControls = true // Enable zoom controls if needed
            displayZoomControls = false // Hide zoom buttons
        }
        webView.webViewClient = WebViewClient() // Ensures content loads within the app
        webView.loadUrl(url)

        if (savedInstanceState != null) {
            // Restore WebView state
            webView.restoreState(savedInstanceState)
        } else {
            // Load the URL for the first time
            val hardcodedUrl = "https://coursemate-chatbot-866939629489.asia-southeast2.run.app/"
            webView.loadUrl(hardcodedUrl)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save the WebView state
        val webView:WebView = binding.wvChatbot
        webView.saveState(outState)
    }
}