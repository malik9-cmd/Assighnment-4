package com.example.androidproject.utils

import android.content.Context
import android.content.SharedPreferences

class PrefsManager(context: Context) {
    private val PREF_NAME = "MyAppPrefs"
    private val KEY_IS_LOGGED_IN = "isLoggedIn"
    private val KEY_THEME = "selectedTheme"

    private val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = prefs.edit()

    var isLoggedIn: Boolean
        get() = prefs.getBoolean(KEY_IS_LOGGED_IN, false)
        set(value) {
            editor.putBoolean(KEY_IS_LOGGED_IN, value).apply()
        }

    var selectedTheme: Int
        get() = prefs.getInt(KEY_THEME, 0) // 0: Light, 1: Dark, 2: Custom
        set(value) {
            editor.putInt(KEY_THEME, value).apply()
        }
        
    companion object {
        const val THEME_LIGHT = 0
        const val THEME_DARK = 1
        const val THEME_CUSTOM = 2
    }
    
    fun clear() {
        editor.clear().apply()
    }
}
