package com.project.playlistmaker.db.mappers

import com.project.playlistmaker.createplaylist.data.db.entity.TrackInPlEntity
import com.project.playlistmaker.db.entity.TrackEntity
import com.project.playlistmaker.searchscreen.domain.models.Track
import java.util.Calendar

class TrackDbMapper {

    fun mapTrackEntityToTrack(from: TrackEntity): Track {
        return Track(
            trackId = from.trackId,
            trackName = from.trackName,
            artistName = from.artistName,
            trackTimeMillis = from.trackTimeMillis,
            artworkUrl100 = from.artworkUrl100,
            collectionName = from.collectionName,
            releaseDate = from.releaseDate,
            primaryGenreName = from.primaryGenreName,
            country = from.country,
            previewUrl = from.previewUrl,
        )
    }

    fun mapTrackToTrackEntity(from: Track): TrackEntity {
        return TrackEntity(
            trackId = from.trackId,
            trackName = from.trackName,
            artistName = from.artistName,
            trackTimeMillis = from.trackTimeMillis,
            artworkUrl100 = from.artworkUrl100,
            collectionName = from.collectionName,
            releaseDate = from.releaseDate,
            primaryGenreName = from.primaryGenreName,
            country = from.country,
            previewUrl = from.previewUrl,
            favouriteAddedTimestamp = Calendar.getInstance().timeInMillis
        )
    }

    fun mapTrackPlToTrack(track: TrackInPlEntity): Track {
        return Track(
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.trackId,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl
        )
    }

    fun mapTrackToTrackPl(track: Track): TrackInPlEntity {
        return TrackInPlEntity(
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.trackId,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl
        )
    }
}