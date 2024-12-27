package com.example.playlistmaker

import android.content.SharedPreferences
import com.example.playlistmaker.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistory(private val sharedPrefs: SharedPreferences) {

    private val gson = Gson()
    private val KEY_SEARCH_HISTORY_LIST = "search_history_list"
    private val MAX_HISTORY_SIZE = 10

    private fun getSharedPrefs(key: String): String? {
        return sharedPrefs.getString(key, null)
    }

    fun arrayListToJson(arrayList: List<Track>): String {
        return gson.toJson(arrayList)
    }

    fun jsonToArrayList(json: String?): ArrayList<Track> {
        return if (json != null) {
            val type = object : TypeToken<ArrayList<Track>>() {}.type
            gson.fromJson(json, type)
        } else {
            ArrayList()
        }
    }

    fun addSong(track: Track) {
        val trackList = getSearchHistory().toMutableList()
        trackList.removeIf { it.trackId == track.trackId }
        trackList.add(0, track)
        if (trackList.size > MAX_HISTORY_SIZE) {
            trackList.removeAt(trackList.size - 1)
        }
        saveSearchHistory(trackList)
    }

    fun getSearchHistory(): List<Track> {
        val jsonHistory = getSharedPrefs(KEY_SEARCH_HISTORY_LIST) ?: return emptyList()
        return jsonToArrayList(jsonHistory)
    }

    private fun saveSearchHistory(songs: List<Track>) {
        val json = arrayListToJson(songs)
        sharedPrefs.edit().putString(KEY_SEARCH_HISTORY_LIST, json).apply()
    }

    fun clearHistory() {
        sharedPrefs.edit().remove(KEY_SEARCH_HISTORY_LIST).apply()
    }
}
