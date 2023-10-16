package com.project.playlistmaker.myplaylists.viewmodel.models

import com.project.playlistmaker.createplaylist.domain.model.Playlist

sealed interface PlaylistState {
    object DisplayEmptyPlaylists : PlaylistState
    class DisplayPlaylists(val playlists: List<Playlist>) : PlaylistState
}