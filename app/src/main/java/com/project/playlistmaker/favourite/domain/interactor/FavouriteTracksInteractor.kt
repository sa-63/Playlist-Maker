package com.project.playlistmaker.favourite.domain.interactor

import com.project.playlistmaker.searchscreen.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavouriteTracksInteractor {
    suspend fun addToFavorites(track: Track)

    suspend fun getFavoritesTracks(): Flow<List<Track>>

    suspend fun deleteFromFavorites(trackId: Int)

    suspend fun isFavoriteTrack(trackId: Int): Flow<Boolean>
}