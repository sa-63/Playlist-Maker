package com.project.playlistmaker

import android.icu.text.SimpleDateFormat
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.util.Locale

class TrackListViewHolder(trackItem: View) : RecyclerView.ViewHolder(trackItem) {

    private val trackName: TextView = trackItem.findViewById(R.id.track_name_tv)
    private val artistName: TextView = trackItem.findViewById(R.id.artist_name_tv)
    private val duration: TextView = trackItem.findViewById(R.id.track_duration_tv)
    private val cover: ImageView = trackItem.findViewById(R.id.track_cover_iv)
    private val cornerSize: Int = itemView.resources.getDimensionPixelSize(R.dimen.cover_corner_size)

    fun bind(trackDto: TrackDto, trackListClickListener: TrackListClickListener) {
        trackName.text = trackDto.trackName
        artistName.text = trackDto.artistName
        duration.text = SimpleDateFormat("mm:ss", Locale.getDefault())
            .format(trackDto.trackTimeMillis)

        Glide.with(itemView)
            .load(trackDto.artworkUrl100)
            .fitCenter()
            .transform(RoundedCorners(cornerSize))
            .placeholder(R.drawable.placeholder)
            .into(cover)

        itemView.setOnClickListener {
            trackListClickListener.setTrackClickListener(trackDto)
        }
    }

    interface TrackListClickListener {
        fun setTrackClickListener(trackDto: TrackDto)
    }
}

