package com.project.playlistmaker.playerscreen.ui.model

import com.project.playlistmaker.createplaylist.domain.model.MyPlaylist

sealed interface PlaylistsInPlayerState {
    class DisplayPlaylists(val myPlaylists: List<MyPlaylist>) : PlaylistsInPlayerState
    object HidePlaylists : PlaylistsInPlayerState
}