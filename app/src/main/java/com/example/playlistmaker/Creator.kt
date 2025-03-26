package com.example.playlistmaker

import com.example.playlistmaker.data.SearchDataImpl.Companion.PLAYLIST_MAKER_PREFERENCES
import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.media.MediaPlayer
import com.example.playlistmaker.data.*
import com.example.playlistmaker.data.network.ITunesAPI
import com.example.playlistmaker.data.network.RetrofitClient
import com.example.playlistmaker.data.network.RetrofitClient.Companion.BASE_URL
import com.example.playlistmaker.domain.api.*
import com.example.playlistmaker.domain.impl.*
import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Creator {

    private lateinit var application: Application

    fun initApplication(application: Application) {
        if (!::application.isInitialized) {
            this.application = application
        }
    }

    private val localResource by lazy {
        SearchDataImpl(application.getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE))
    }

    private val gson by lazy { Gson() }

    private val apiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesAPI::class.java)
    }

    private val retrofitClient by lazy { RetrofitClient(apiService) }
    private val historyRepository by lazy { HistoryRepositoryImpl(localResource) }
    private val trackRepository by lazy { TracksRepositoryImpl(retrofitClient) }
    private val handlerRepository by lazy { HandlerRepositoryImpl(application) }
    private val themeRepository by lazy { ThemeRepositoryImpl(localResource, application) }
    private val mediaPlayer by lazy { MediaPlayer() }

    fun themeInteractorDo(): ThemeInteractor = ThemeInteractorImpl(themeRepository)
    fun handlerInteractorDo(): HandlerInteractor = HandlerInteractorImpl(handlerRepository)
    fun trackInteractorDo(): TrackInteractor = TrackInteractorImpl(trackRepository)
    fun historyInteractorDo(): HistoryInteractor = HistoryInteractorImpl(historyRepository)
    fun playerInteractorDo(): PlayerInteractor = PlayerInteractorImpl(PlayerRepositoryImpl(mediaPlayer))
}
