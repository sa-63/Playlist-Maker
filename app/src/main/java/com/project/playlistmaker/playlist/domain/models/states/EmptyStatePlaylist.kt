package com.project.playlistmaker.playlist.domain.models.states

import com.project.playlistmaker.playlist.domain.models.states.entity.Playlist

sealed class EmptyStatePlaylist {
    class EmptyPlaylist : EmptyStatePlaylist()
    class NotEmptyPlaylist(val playlist: List<Playlist>) : EmptyStatePlaylist()
}