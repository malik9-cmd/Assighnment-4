package com.example.androidproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.androidproject.utils.PrefsManager
import com.example.androidproject.R
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {

    private lateinit var prefsManager: PrefsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        // Apply theme before creation if needed, but Login usually has standard theme
        // Check login state first
        prefsManager = PrefsManager(this)
        applySavedTheme() 
        
        super.onCreate(savedInstanceState)
        
        if (prefsManager.isLoggedIn) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_login)

        val etUsername = findViewById<TextInputEditText>(R.id.etUsername)
        val etPassword = findViewById<TextInputEditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                // Dummy authentication
                prefsManager.isLoggedIn = true
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Please enter valid credentials", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
     private fun applySavedTheme() {
        when (prefsManager.selectedTheme) {
            PrefsManager.THEME_LIGHT -> setTheme(R.style.Theme_AndroidProject_Light)
            PrefsManager.THEME_DARK -> setTheme(R.style.Theme_AndroidProject_Dark)
            PrefsManager.THEME_CUSTOM -> setTheme(R.style.Theme_AndroidProject_Custom)
            else -> setTheme(R.style.Theme_AndroidProject_Light)
        }
    }
}
