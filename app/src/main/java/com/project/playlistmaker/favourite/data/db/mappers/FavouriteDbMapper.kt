package com.project.playlistmaker.favourite.data.db.mappers

import com.project.playlistmaker.favourite.data.db.entity.FavouriteEntity
import com.project.playlistmaker.searchscreen.domain.models.Track
import java.util.Calendar

class FavouriteDbMapper {
    fun mapTrackEntityToTrack(from: FavouriteEntity): Track {
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

    fun mapTrackToTrackEntity(from: Track): FavouriteEntity {
        return FavouriteEntity(
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
}