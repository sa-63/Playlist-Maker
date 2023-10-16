package com.project.playlistmaker.db.mappers

import com.project.playlistmaker.createplaylist.domain.model.Playlist
import com.project.playlistmaker.db.entity.PlaylistEntity

class PlaylistDbMapper {
    fun mapToPlaylistEntity(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            name = playlist.name,
            description = playlist.description,
            coverPath = playlist.coverUri,
            numberOfTracks = playlist.numberOfTracks
        )
    }

    fun mapToPlaylist(playlist: PlaylistEntity): Playlist {
        return Playlist(
            playlistId = playlist.playlistId,
            name = playlist.name,
            description = playlist.description,
            coverUri = playlist.coverUri,
            tracks = ArrayList(),
            numberOfTracks = playlist.numberOfTracks
        )
    }
}