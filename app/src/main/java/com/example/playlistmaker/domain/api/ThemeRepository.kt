package com.example.playlistmaker.domain.api

interface ThemeRepository {
    fun getCurrentTheme(): Boolean
    fun applyTheme()
    fun switchTheme(darkThemeEnabled: Boolean)
}