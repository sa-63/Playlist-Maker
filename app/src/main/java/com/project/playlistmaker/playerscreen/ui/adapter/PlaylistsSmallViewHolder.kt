package com.project.playlistmaker.playerscreen.ui.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.project.playlistmaker.R
import com.project.playlistmaker.createplaylist.domain.model.MyPlaylist
import com.project.playlistmaker.utils.TextUtils

class PlaylistsSmallViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val coverImage = itemView.findViewById<ImageView>(R.id.playlist_cover_small)
    private val name = itemView.findViewById<TextView>(R.id.playlist_name_small)
    private val numberOfTracks = itemView.findViewById<TextView>(R.id.number_of_tracks_small)

    fun bind(myPlaylist: MyPlaylist) {
        Glide.with(coverImage)
            .load(myPlaylist.coverUri)
            .transform(
                CenterCrop(),
                RoundedCorners(itemView.resources.getDimensionPixelSize(R.dimen.rounded_corners_album_preview))
            )
            .placeholder(R.drawable.ic_track_placeholder_small)
            .into(coverImage)
        name.text = myPlaylist.name
        numberOfTracks.text = TextUtils.numberOfTracksString(myPlaylist.numberOfTracks)
    }
}