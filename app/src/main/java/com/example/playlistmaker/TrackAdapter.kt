package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.Track
import com.example.playlistmaker.TrackViewHolder

class TrackAdapter(
    private var trackListResult: MutableList<Track>,
    private val onSongClick: (Track) -> Unit
) : RecyclerView.Adapter<TrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder.create(parent)
    }

    override fun getItemCount(): Int {
        return trackListResult.size
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(trackListResult[position], holder.itemView.context)
        holder.itemView.setOnClickListener {
            onSongClick(trackListResult[position])
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newTracks: List<Track>) {
        trackListResult.clear()
        trackListResult.addAll(newTracks)
        notifyDataSetChanged()
    }

    // Обновленный метод добавления трека в начало списка
    fun addTrack(track: Track) {
        // Убираем трек, если он уже существует
        trackListResult.removeIf { it.trackId == track.trackId }

        // Добавляем трек в начало списка
        trackListResult.add(0, track)
        notifyItemInserted(0) // Уведомляем о добавлении в начало
    }

    fun removeTrack(position: Int) {
        if (position >= 0 && position < trackListResult.size) {
            trackListResult.removeAt(position)
            notifyItemRemoved(position)
        }
    }
}
