package com.example.playlistmaker

import android.content.Context
import android.net.ConnectivityManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val albumImage: ImageView = itemView.findViewById(R.id.track_albumImage)
    private val trackName: TextView = itemView.findViewById(R.id.track_name)
    private val trackArtist: TextView = itemView.findViewById(R.id.track_artist)
    private val trackDuration: TextView = itemView.findViewById(R.id.track_duration)

    fun bind(model: Track, context: Context) {
        trackName.text = model.trackName
        trackArtist.text = model.artistName
        trackDuration.text = model.trackTime

        if (isInternetAvailable(context)) {
            Glide.with(itemView)
                .load(model.artworkUrl100)
                .placeholder(R.drawable.stub_album)
                .transform(CenterCrop(), RoundedCorners(2))
                .into(albumImage)
        } else {
            albumImage.setImageResource(R.drawable.stub_album)
        }
    }

    private fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected
    }

    companion object {
        fun create(parent: ViewGroup): TrackViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.search_for_track, parent, false)
            return TrackViewHolder(view)
        }
    }
}