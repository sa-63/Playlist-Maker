package com.project.playlistmaker.presentation.adapters.track_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.project.playlistmaker.R
import com.project.playlistmaker.domain.models.Track

class TrackListAdapter(
    private val tracksList: ArrayList<Track>,
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
