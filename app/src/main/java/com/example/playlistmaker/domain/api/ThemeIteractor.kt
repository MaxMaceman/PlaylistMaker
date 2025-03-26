package com.example.playlistmaker.domain.api

interface ThemeInteractor {
    fun getCurrentTheme(): Boolean
    fun applyTheme()
    fun switchTheme(darkThemeEnabled: Boolean)
}