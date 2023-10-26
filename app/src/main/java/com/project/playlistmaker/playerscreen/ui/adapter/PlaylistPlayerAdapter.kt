package com.project.playlistmaker.playerscreen.ui.adapter

import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.project.playlistmaker.playlist.domain.models.entity.Playlist
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlaylistPlayerAdapter(
    private val listPlaylists: List<Playlist>,
    private val lifecycleScope: () -> LifecycleCoroutineScope,
    private val listener: PlaylistClickListener
) : RecyclerView.Adapter<PlaylistPlayerViewHolder>() {

    private var isClickAllowed = true

    override fun onCreateViewHolder(
        parentView: ViewGroup,
        viewType: Int
    ): PlaylistPlayerViewHolder {

        return PlaylistPlayerViewHolder(parentView)
    }

    override fun onBindViewHolder(holder: PlaylistPlayerViewHolder, position: Int) {

        holder.bind(listPlaylists[position])

        holder.itemView.setOnClickListener {

            if (clickDebounce()) {

                listener.onClickView(listPlaylists[position])

            }
        }
    }

    override fun getItemCount(): Int {
        return listPlaylists.size
    }

    private fun clickDebounce(): Boolean {

        val isCurrentAllowedClick = isClickAllowed

        if (isClickAllowed) {
            isClickAllowed = false

            lifecycleScope.invoke().launch {
                delay(CLICK_DEBOUNCE_DELAY_MILLIS)
                isClickAllowed = true
            }
        }

        return isCurrentAllowedClick
    }

    interface PlaylistClickListener {
        fun onClickView(playlist: Playlist)
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
    }
}