package com.example.playlistmaker.data

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.domain.api.ThemeRepository
import com.example.playlistmaker.ui.main.IS_DARK

class ThemeRepositoryImpl(private val localResource: SearchDataImpl, private val application: Application) : ThemeRepository {

    private var darkTheme: Boolean = false

    override fun getCurrentTheme(): Boolean {
        return if (localResource.contains(IS_DARK)) {
            localResource.getBoolean(IS_DARK, false)
        } else {
            val isSystemDark = (application.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
            localResource.putBoolean(IS_DARK, isSystemDark)
            isSystemDark
        }
    }

    override fun applyTheme() {
        val isDarkTheme = getCurrentTheme()
        switchTheme(isDarkTheme)
    }

    override fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        localResource.putBoolean(IS_DARK, darkTheme)
    }
}
