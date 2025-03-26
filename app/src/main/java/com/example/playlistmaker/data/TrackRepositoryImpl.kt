package com.example.playlistmaker.data

import com.example.playlistmaker.data.dto.TrackSearchRequest
import com.example.playlistmaker.data.dto.TrackSearchResponce
import com.example.playlistmaker.domain.api.TrackRepository
import com.example.playlistmaker.domain.models.Track

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {

    override fun search(expression: String): List<Track> {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        if (response.resultCode == 200) {
            return (response as TrackSearchResponce).result.map {
                Track(
                    trackId = it.trackId.toInt(),
                    trackName = it.trackName,
                    artistName = it.artistName,
                    collectionName = it.collectionName,
                    releaseDate = it.releaseDate,
                    primaryGenreName = it.primaryGenreName ?: "Unknown",
                    country = it.country,
                    trackTimeMillis = it.trackTimeMillis?.toLong() ?: 0L,
                    artworkUrl100 = it.artworkUrl100,
                    previewUrl = it.previewUrl
                )
            }
        } else {
            return emptyList()
        }
    }
}