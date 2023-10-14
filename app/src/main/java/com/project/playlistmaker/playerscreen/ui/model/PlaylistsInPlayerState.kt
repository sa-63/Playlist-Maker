package com.project.playlistmaker.playerscreen.ui.model

import com.project.playlistmaker.createplaylist.domain.model.Playlist

sealed interface PlaylistsInPlayerState {
    class DisplayPlaylists(val playlists: List<Playlist>) : PlaylistsInPlayerState
    object HidePlaylists : PlaylistsInPlayerState
}