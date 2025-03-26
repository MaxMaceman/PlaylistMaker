package com.example.playlistmaker

import android.app.Application
import com.example.playlistmaker.domain.api.ThemeInteractor


const val ADD_TRACK_KEY = "switch_track"

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Creator.initApplication(this)

        val themeInteractor: ThemeInteractor = Creator.themeInteractorDo()
        themeInteractor.applyTheme()
    }
}