package com.project.playlistmaker.playlist.domain.models.states

import com.project.playlistmaker.createplaylist.domain.model.MyPlaylist

sealed class EmptyStatePlaylist {
    class EmptyPlaylist: EmptyStatePlaylist()
    class NotEmptyPlaylist(val myPlaylist: List<MyPlaylist>): EmptyStatePlaylist()
}