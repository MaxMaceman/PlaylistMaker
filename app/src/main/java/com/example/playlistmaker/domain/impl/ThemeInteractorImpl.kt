package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.ThemeInteractor
import com.example.playlistmaker.domain.api.ThemeRepository

class ThemeInteractorImpl(private val theme: ThemeRepository): ThemeInteractor {
    override fun getCurrentTheme(): Boolean {
        return theme.getCurrentTheme()
    }

    override fun applyTheme() {
        theme.applyTheme()
    }

    override fun switchTheme(darkThemeEnabled: Boolean) {
        theme.switchTheme(darkThemeEnabled)
    }
}