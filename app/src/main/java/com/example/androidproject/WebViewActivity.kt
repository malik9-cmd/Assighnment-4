package com.example.androidproject

import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity

class WebViewActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_URL = "extra_url"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)

        val webView = findViewById<WebView>(R.id.webView)
        val progressBar = findViewById<ProgressBar>(R.id.progressBarWeb)
        
        val url = intent.getStringExtra(EXTRA_URL) ?: "https://www.google.com"

        webView.settings.javaScriptEnabled = true
        
        webView.webViewClient = object : WebViewClient() {
             override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url ?: "")
                return true
            }
        }
        
        webView.webChromeClient = object : WebChromeClient() {
             override fun onProgressChanged(view: WebView?, newProgress: Int) {
                if (newProgress == 100) {
                    progressBar.visibility = android.view.View.GONE
                } else {
                    progressBar.visibility = android.view.View.VISIBLE
                    progressBar.progress = newProgress
                }
            }
        }
        
        webView.loadUrl(url)
    }
}
