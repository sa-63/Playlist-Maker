package com.project.playlistmaker.mediascreen.domain.impl

import com.project.playlistmaker.mediascreen.domain.interactor.FavouriteTracksInteractor
import com.project.playlistmaker.mediascreen.domain.repository.FavouriteTracksRepository
import com.project.playlistmaker.searchscreen.domain.models.Track
import kotlinx.coroutines.flow.Flow

class FavouriteTracksInteractorImpl(
    private val favouriteTracksRepository: FavouriteTracksRepository
) : FavouriteTracksInteractor {

    override suspend fun addToFavorites(track: Track) {
        favouriteTracksRepository.addToFavorites(track)
    }

    override suspend fun getFavoritesTracks(): Flow<List<Track>> {
        return favouriteTracksRepository.getFavoritesTracks()
    }

    override suspend fun deleteFromFavorites(trackId: Int) {
        favouriteTracksRepository.deleteFromFavorites(trackId)
    }

    override suspend fun isFavoriteTrack(trackId: Int): Flow<Boolean> {
        return favouriteTracksRepository.isFavoriteTrack(trackId)
    }
}