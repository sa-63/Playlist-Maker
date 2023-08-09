package com.project.playlistmaker.search_screen.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.playlistmaker.R
import com.project.playlistmaker.search_screen.domain.models.Track
import com.project.playlistmaker.search_screen.ui.view_holder.TrackListViewHolder

class TrackListAdapter(
    private val tracksList: List<Track>,
    private val trackListClickListener: TrackListViewHolder.TrackListClickListener
) : RecyclerView.Adapter<TrackListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return TrackListViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackListViewHolder, position: Int) {
        holder.bind(tracksList[position], trackListClickListener)
    }

    override fun getItemCount(): Int {
        return tracksList.size
    }
}
