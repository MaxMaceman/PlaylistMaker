package com.example.playlistmaker.ui.player

import android.os.Bundle
import android.os.Looper
import android.os.Handler
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.ADD_TRACK_KEY
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.api.PlayerInteractor
import com.example.playlistmaker.domain.models.Track
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    private lateinit var playerInteractor: PlayerInteractor
    private lateinit var playButton: ImageView
    private lateinit var timeScore: TextView
    private val handler = Handler(Looper.getMainLooper())

    private var playerState = STATE_DEFAULT

    private val updateTimeRunnable = object : Runnable {
        override fun run() {
            timeScore.text = SimpleDateFormat("mm:ss", Locale.getDefault())
                .format(playerInteractor.currentPosition())
            handler.postDelayed(this, 500L)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        playerInteractor = Creator.playerInteractorDo()

        playButton = findViewById(R.id.play_button)
        findViewById<androidx.appcompat.widget.Toolbar>(R.id.player_toolbar).apply {
            setNavigationOnClickListener { finish() }
        }

        timeScore = findViewById(R.id.score_bar)

        intent.getStringExtra(ADD_TRACK_KEY)?.let { trackJson ->
            val track = Gson().fromJson(trackJson, Track::class.java)
            displayTrackInfo(track)
        }

        playButton.setOnClickListener {
            if (playerInteractor.playbackControl()) {
                playButton.setImageResource(R.drawable.pause)
                handler.post(updateTimeRunnable)
            } else {
                playButton.setImageResource(R.drawable.play_btn_light)
                handler.removeCallbacks(updateTimeRunnable)
            }
        }
    }

    private fun displayTrackInfo(track: Track) {
        findViewById<TextView>(R.id.playlist_track).text = track.trackName
        findViewById<TextView>(R.id.playlist_artist).text = track.artistName
        findViewById<TextView>(R.id.duration_value).text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
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

        preparePlayer(track)
    }

    private fun preparePlayer(track: Track) {
        playerInteractor.preparePlayer(
            track.previewUrl,
            onPrepared = {
                playButton.isEnabled = true
            },
            onCompletion = {
                playButton.setImageResource(R.drawable.play_btn_light)
                timeScore.text = "00:00"
                handler.removeCallbacks(updateTimeRunnable)
            }
        )
    }

    override fun onPause() {
        super.onPause()
        playerInteractor.pause()
        handler.removeCallbacks(updateTimeRunnable)
        playButton.setImageResource(R.drawable.play_btn_light)
    }

    override fun onDestroy() {
        super.onDestroy()
        playerInteractor.release()
        handler.removeCallbacks(updateTimeRunnable)
    }

    companion object {
        private const val STATE_DEFAULT = 0
    }
}
