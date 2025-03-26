package com.example.playlistmaker.domain.api

interface PlayerInteractor {
    fun preparePlayer(url: String, onPrepared: () -> Unit, onCompletion: () -> Unit)
    fun start()
    fun pause()
    fun release()
    fun currentPosition(): Long
    fun playbackControl(): Boolean
}