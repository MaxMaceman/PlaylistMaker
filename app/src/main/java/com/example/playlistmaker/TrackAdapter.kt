package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.Track
import com.example.playlistmaker.TrackViewHolder

class TrackAdapter(
    private var trackListResult: ArrayList<Track>,
    private val onSongClick: (Track) -> Unit
) : RecyclerView.Adapter<TrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder.create(parent)
    }

    override fun getItemCount(): Int {
        return trackListResult.size
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(trackListResult.get(position), holder.itemView.context)
        holder.itemView.setOnClickListener { onSongClick(trackListResult[position]) }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newTracks: List<Track>) {
        trackListResult.clear()
        trackListResult.addAll(newTracks)
        notifyDataSetChanged()
    }

    fun addTrack(track: Track) {
        trackListResult.add(track)
        notifyItemInserted(trackListResult.size - 1)
    }

    fun removeTrack(position: Int) {
        if (position >= 0 && position < trackListResult.size) {
            trackListResult.removeAt(position)
            notifyItemRemoved(position)
        }
    }
}