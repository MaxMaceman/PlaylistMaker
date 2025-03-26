package com.example.playlistmaker.data.dto

class TrackSearchResponce(val searchType: String, val expression: String, val result: List<TrackDto>) : Response()