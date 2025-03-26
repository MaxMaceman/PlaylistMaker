package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface HistoryInteractor {
    fun getSearchHistory(): MutableList<Track>
    fun addSong(track: Track)
    fun inspectAndSave(track: Track, tracks: MutableList<Track>): MutableList<Track>
    fun saveSearchHistory(tracks: MutableList<Track>)
    fun clearHistory()
}