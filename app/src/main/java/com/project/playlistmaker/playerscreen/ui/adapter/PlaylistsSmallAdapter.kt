package com.project.playlistmaker.playerscreen.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.playlistmaker.R
import com.project.playlistmaker.createplaylist.domain.model.MyPlaylist

class PlaylistsSmallAdapter(private val clickListener: PlaylistSmallClickListener) :
    RecyclerView.Adapter<PlaylistsSmallViewHolder>() {

    val myPlaylists = ArrayList<MyPlaylist>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistsSmallViewHolder =
        PlaylistsSmallViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.playlist_card_view_small, parent, false)
        )

    override fun onBindViewHolder(holder: PlaylistsSmallViewHolder, position: Int) {
        holder.bind(myPlaylists[position])
        holder.itemView.setOnClickListener { clickListener.onPlaylistClickListener(myPlaylists[position]) }
    }

    override fun getItemCount(): Int = myPlaylists.size

    fun interface PlaylistSmallClickListener {
        fun onPlaylistClickListener(myPlaylist: MyPlaylist)
    }
}