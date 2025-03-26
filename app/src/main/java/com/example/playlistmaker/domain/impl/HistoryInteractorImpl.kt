package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.HistoryInteractor
import com.example.playlistmaker.domain.api.HistoryRepository
import com.example.playlistmaker.domain.models.Track

class HistoryInteractorImpl(private val repository: HistoryRepository) :
    HistoryInteractor {
    override fun getSearchHistory(): MutableList<Track> {
        return repository.getSearchHistory()
    }

    override fun addSong(track: Track) {
        repository.addSong(track)
    }

    override fun inspectAndSave(
        track: Track,
        tracks: MutableList<Track>
    ): MutableList<Track> {
        return repository.inspectAndSave(track, tracks)
    }

    override fun saveSearchHistory(tracks: MutableList<Track>) {
        repository.saveSearchHistory(tracks)
    }

    override fun clearHistory() {
        repository.clearHistory()
    }
}