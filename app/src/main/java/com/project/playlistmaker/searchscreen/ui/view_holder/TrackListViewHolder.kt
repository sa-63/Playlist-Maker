package com.project.playlistmaker.searchscreen.ui.view_holder

import android.icu.text.SimpleDateFormat
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.project.playlistmaker.R
import com.project.playlistmaker.searchscreen.domain.models.Track
import java.util.Locale

class TrackListViewHolder(trackItem: View) : RecyclerView.ViewHolder(trackItem) {

    private val trackName: TextView = trackItem.findViewById(R.id.track_name_tv)
    private val artistName: TextView = trackItem.findViewById(R.id.artist_name_tv)
    private val duration: TextView = trackItem.findViewById(R.id.track_duration_tv)
    private val cover: ImageView = trackItem.findViewById(R.id.track_cover_iv)
    private val cornerSize: Int = itemView.resources.getDimensionPixelSize(R.dimen.cover_corner_size)

    fun bind(track: Track, trackListClickListener: TrackListClickListener) {
        trackName.text = track.trackName
        artistName.text = track.artistName
        duration.text = SimpleDateFormat("mm:ss", Locale.getDefault())
            .format(track.trackTimeMillis)

        Glide.with(itemView)
            .load(track.artworkUrl100)
            .fitCenter()
            .transform(RoundedCorners(cornerSize))
            .placeholder(R.drawable.placeholder)
            .into(cover)

        itemView.setOnClickListener {
            trackListClickListener.setTrackClickListener(track)
        }
    }

    interface TrackListClickListener {
        fun setTrackClickListener(track: Track)
    }
}

