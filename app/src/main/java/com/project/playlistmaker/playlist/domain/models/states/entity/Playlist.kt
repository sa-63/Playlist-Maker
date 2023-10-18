package com.project.playlistmaker.playlist.domain.models.states.entity

data class Playlist(
    val id: Int? = null,
    val playlistName: String? = null,
    val descriptionPlaylist: String? = null,
    var imageInStorage: String? = null,
    val tracksInPlaylist: ArrayList<Long>? = null,
    val countTracks: Int = 0
)