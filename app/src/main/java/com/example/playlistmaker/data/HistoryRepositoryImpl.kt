package com.example.playlistmaker.data

import com.example.playlistmaker.domain.api.HistoryRepository
import com.example.playlistmaker.domain.api.SearchData
import com.example.playlistmaker.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class HistoryRepositoryImpl(private val searchData: SearchData) : HistoryRepository {

    private val gson = Gson()

    private fun getSharedPrefs(key: String): String? {
        return searchData.getString(key, null)
    }

    fun arrayListToJson(arrayList: MutableList<Track>): String {
        return gson.toJson(arrayList)
    }

    fun jsonToArrayList(json: String?): MutableList<Track> {
        return if (json != null) {
            val type = object : TypeToken<MutableList<Track>>() {}.type
            gson.fromJson(json, type)
        } else {
            mutableListOf()
        }
    }

    override fun addSong(track: Track) {
        val trackList = getSearchHistory()
        trackList.removeIf { it.trackId == track.trackId }
        trackList.add(0, track)
        if (trackList.size > MAX_HISTORY_SIZE) {
            trackList.removeAt(trackList.size - 1)
        }
        saveSearchHistory(trackList)
    }

    override fun getSearchHistory(): MutableList<Track> {
        val jsonHistory = getSharedPrefs(KEY_SEARCH_HISTORY_LIST) ?: return mutableListOf()
        return jsonToArrayList(jsonHistory)
    }

    override fun saveSearchHistory(songs: MutableList<Track>) {
        val json = arrayListToJson(songs)
        searchData.putString(KEY_SEARCH_HISTORY_LIST, json)
    }

    override fun inspectAndSave(track: Track, tracks: MutableList<Track>): MutableList<Track> {
        tracks.removeIf { it.trackId == track.trackId }
        tracks.add(0, track)
        if (tracks.size > MAX_HISTORY_SIZE) {
            tracks.removeAt(tracks.size - 1)
        }
        saveSearchHistory(tracks)
        return tracks
    }

    override fun clearHistory() {
        searchData.remove(KEY_SEARCH_HISTORY_LIST)
    }

    companion object {
        private const val KEY_SEARCH_HISTORY_LIST = "search_history_list"
        private const val MAX_HISTORY_SIZE = 10
    }
}
