package com.example.playlistmaker

data class Track (
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val collectionName: String,
    val releaseDate: Int,
    val primaryGenreName: String,
    val country: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String
)
