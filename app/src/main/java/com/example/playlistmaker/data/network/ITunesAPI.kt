package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.dto.TrackSearchResponce
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesAPI {
    @GET("/search")
    fun search(@Query("term") text: String): Call<TrackSearchResponce>
}