package com.example.playlistmaker

import android.os.Build
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        findViewById<androidx.appcompat.widget.Toolbar>(R.id.player_toolbar).apply {
            setNavigationOnClickListener { finish() }
        }

        intent.getStringExtra(ADD_TRACK_KEY)?.let { trackJson ->
            val track = Gson().fromJson(trackJson, Track::class.java)
            displayTrackInfo(track)
        }
    }

    private fun displayTrackInfo(track: Track) {
        findViewById<TextView>(R.id.playlist_track).text = track.trackName
        findViewById<TextView>(R.id.playlist_artist).text = track.artistName
        findViewById<TextView>(R.id.duration_value).text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
        findViewById<TextView>(R.id.album_value).text = track.collectionName
        findViewById<TextView>(R.id.genre_value).text = track.primaryGenreName
        findViewById<TextView>(R.id.country_value).text = track.country

        val yearView = findViewById<TextView>(R.id.year_value)
        val originalFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        originalFormat.timeZone = java.util.TimeZone.getTimeZone("UTC")
        val targetFormat = SimpleDateFormat("yyyy", Locale.getDefault())
        yearView.text = try {
            val date = originalFormat.parse(track.releaseDate)
            targetFormat.format(date ?: "")
        } catch (e: Exception) {
            ""
        }

        val albumCover = findViewById<ImageView>(R.id.playlist_album)
        val imageUrl = track.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg")

        Glide.with(this)
            .load(imageUrl)
            .transform(CenterCrop(), RoundedCorners(2))
            .placeholder(R.drawable.album_placeholder)
            .into(albumCover)
    }
}
