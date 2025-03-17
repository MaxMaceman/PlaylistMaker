package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate

const val ADD_TRACK_KEY = "switch_track"

class App : Application() {
    private var darkTheme: Boolean = false
    private val sharedPreferences: SharedPreferences by lazy {
        getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
    }

    override fun onCreate() {
        super.onCreate()
        applyTheme()
    }

    fun getCurrentTheme(): Boolean {
        return if (sharedPreferences.contains(IS_DARK)) {
            sharedPreferences.getBoolean(IS_DARK, darkTheme)
        } else {
            val isSystemDark = (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
            sharedPreferences.edit().putBoolean(IS_DARK, isSystemDark).apply()
            isSystemDark
        }
    }

    fun applyTheme() {
        val isDarkTheme = getCurrentTheme()
        switchTheme(isDarkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        sharedPreferences.edit().putBoolean(IS_DARK, darkTheme).apply()
    }
}