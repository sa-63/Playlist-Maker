package com.project.playlistmaker.playlist.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.project.playlistmaker.R
import com.project.playlistmaker.searchscreen.domain.models.Track
import com.project.playlistmaker.utils.Formatter

class TracksViewHolder(
    parentView: ViewGroup,
    trackItem: View = LayoutInflater.from(parentView.context)
        .inflate(R.layout.track_item, parentView, false)
) : RecyclerView.ViewHolder(trackItem) {

    private val trackName: TextView = trackItem.findViewById(R.id.track_name_tv)
    private val artistName: TextView = trackItem.findViewById(R.id.artist_name_tv)
    private val duration: TextView = trackItem.findViewById(R.id.track_duration_tv)
    private val cover: ImageView = trackItem.findViewById(R.id.track_cover_iv)
    private val cornerSize: Int =
        itemView.resources.getDimensionPixelSize(R.dimen.cover_corner_size)

    fun bind(trackData: Track) {
        trackName.text = trackData.trackName
        artistName.text = trackData.artistName
        duration.text = Formatter.dateFormatting(trackData.trackTimeMillis)

        Glide.with(itemView)
            .load(trackData.getCover60())
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(cornerSize))
            .into(cover)
    }
}