package com.project.playlistmaker.playlist.ui.adapters

import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.project.playlistmaker.searchscreen.domain.models.Track
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlaylistTracksAdapter(
    private val listTracks: List<Track>,
    private val lifecycleScope: () -> LifecycleCoroutineScope,
    private val listener: ClickTrackListener
) : RecyclerView.Adapter<TracksViewHolder>() {

    private var isClickAllowed = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder {
        return TracksViewHolder(parent)
    }

    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {
        holder.bind(listTracks[position])
        holder.itemView.setOnClickListener {
            if (clickDebounce()) {
                listener.onShortClickView(listTracks[position])
            }
        }
        holder.itemView.setOnLongClickListener {
            listener.onLongClickView(listTracks[position].trackId)
        }
    }

    override fun getItemCount(): Int {
        return listTracks.size
    }

    private fun clickDebounce(): Boolean {
        val isCurrentAllowedClick = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false

            lifecycleScope.invoke().launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return isCurrentAllowedClick
    }

    interface ClickTrackListener {
        fun onShortClickView(track: Track)
        fun onLongClickView(idTrack: Long?): Boolean
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}