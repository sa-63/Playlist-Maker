package com.project.playlistmaker.favourite.domain.repository

import com.project.playlistmaker.searchscreen.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavouriteTracksRepository {
    suspend fun addToFavorites(track: Track)

    suspend fun getFavoritesTracks(): Flow<List<Track>>

    suspend fun deleteFromFavorites(trackId: Long?)

    suspend fun isFavoriteTrack(trackId: Long?): Flow<Boolean>
}