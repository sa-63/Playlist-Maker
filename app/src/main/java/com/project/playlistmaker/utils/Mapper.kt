package com.project.playlistmaker.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.project.playlistmaker.playlist.data.models.PlaylistEntity
import com.project.playlistmaker.playlist.data.models.TrackEntityInPlaylist
import com.project.playlistmaker.playlist.domain.models.states.entity.Playlist
import com.project.playlistmaker.searchscreen.domain.models.Track

object Mapper {
    val gson = Gson()

    fun getArrayPlaylistFromPlaylistEntity(playlist: List<PlaylistEntity>): List<Playlist> {

        return playlist.map {
            Playlist(
                id = it.id,
                playlistName = it.playlistName,
                descriptionPlaylist = it.descriptionPlaylist,
                imageInStorage = it.imageInStorage,
                tracksInPlaylist = takeFromJson(it.tracksInPlaylist),
                countTracks = it.countTracks
            )

        }
    }

    fun getTrackEntityFromTrack(track: Track): TrackEntityInPlaylist {
        return TrackEntityInPlaylist(
            trackId = track.trackId,
            isFavorite = track.isFavorite,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis,
            artworkUrl100 = track.artworkUrl100,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            previewUrl = track.previewUrl
        )
    }

    fun getArrayTrackFromTrackEntityPlaylist(trackEntities: List<TrackEntityInPlaylist>): List<Track> {
        return trackEntities.map {
            Track(
                trackId = it.trackId,
                isFavorite = it.isFavorite,
                trackName = it.trackName,
                artistName = it.artistName,
                trackTimeMillis = it.trackTimeMillis,
                artworkUrl100 = it.artworkUrl100,
                collectionName = it.collectionName,
                releaseDate = it.releaseDate,
                primaryGenreName = it.primaryGenreName,
                country = it.country,
                previewUrl = it.previewUrl
            )
        }
    }

    fun getPlaylistFromPlaylistEntity(playlist: PlaylistEntity): Playlist {
        return Playlist(
            id = playlist.id,
            playlistName = playlist.playlistName,
            descriptionPlaylist = playlist.descriptionPlaylist,
            imageInStorage = playlist.imageInStorage,
            tracksInPlaylist = takeFromJson(playlist.tracksInPlaylist),
            countTracks = playlist.countTracks
        )
    }

    fun getPlaylistEntityFromPlaylist(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            playlistName = playlist.playlistName,
            descriptionPlaylist = playlist.descriptionPlaylist,
            imageInStorage = playlist.imageInStorage
        )
    }


    fun takeFromJson(jsonString: String?): ArrayList<Long> {

        if (jsonString != null) {
            val itemType = object : TypeToken<ArrayList<Long>>() {}.type

            return gson.fromJson(jsonString, itemType)
        }
        return ArrayList<Long>()
    }

    fun toJsonFromArray(listId: ArrayList<Long>): String {
        return gson.toJson(listId)
    }

}