package com.project.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackListViewHolder(trackItem: View) : RecyclerView.ViewHolder(trackItem) {

    private val trackName: TextView = trackItem.findViewById(R.id.track_name_tv)
    private val artistName: TextView = trackItem.findViewById(R.id.artist_name_tv)
    private val duration: TextView = trackItem.findViewById(R.id.track_duration_tv)
    private val cover: ImageView = trackItem.findViewById(R.id.track_cover_iv)

    fun bind(trackDto: TrackDto) {
        trackName.text = trackDto.trackName
        artistName.text = trackDto.artistName
        duration.text = trackDto.trackTime

        Glide.with(itemView)
            .load(trackDto.artworkUrl100)
            .fitCenter()
            .transform(RoundedCorners(2))
            .placeholder(R.drawable.placeholder)
            .into(cover)
    }
}
