package com.example.playlistmaker

import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.os.Handler
import android.widget.ImageView
import android.widget.TextView
import android.widget.ImageButton
import android.media.MediaPlayer
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

private var mediaPlayer = MediaPlayer()
private lateinit var playButton: ImageView
private lateinit var handler: Handler
private lateinit var timeScore: TextView
private lateinit var  setTimeScore: Runnable

class PlayerActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        playButton = findViewById(R.id.play_button)
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
            preparePlayer(track)

        playButton.setOnClickListener{
            when (playerState) {
                STATE_PLAYING -> pause()
                STATE_PREPARED,
                STATE_PAUSED -> play()
            }
        }

        timeScore = findViewById<TextView>(R.id.score_bar)
        handler = Handler(Looper.getMainLooper())
        setTimeScore = Runnable { timeScore() }

    }

    private fun preparePlayer(track: Track){
        mediaPlayer = MediaPlayer()
        mediaPlayer.reset()
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playButton.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {

            playerState = STATE_PREPARED
            handler.removeCallbacks(setTimeScore)
            playButton.setImageResource(R.drawable.play_btn_light)
            timeScore.text = "00:00"
        }
    }

    private fun play(){
        mediaPlayer.start()
        playerState = STATE_PLAYING
        playButton.setImageResource(R.drawable.pause)
        handler.post(setTimeScore)
    }

    private fun pause(){
        mediaPlayer.pause()
        playerState = STATE_PAUSED
        playButton.setImageResource((R.drawable.play_btn_light))
        handler.removeCallbacks(setTimeScore)
    }

    private fun timeScore(){
        val time = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
        timeScore.text = time
        handler.postDelayed(setTimeScore,500L)
    }

    override fun onPause(){
        super.onPause()
        pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        handler.removeCallbacks(setTimeScore)
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }
    private var playerState = STATE_DEFAULT
}

