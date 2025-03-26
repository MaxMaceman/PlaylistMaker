package com.example.playlistmaker.domain.api

interface PlayerRepository {
    fun preparePlayer(url: String, onPrepared: () -> Unit, onCompletion: () -> Unit)
    fun start()
    fun pause()
    fun release()
    fun playbackControl(): Boolean
    fun currentPosition(): Long

}