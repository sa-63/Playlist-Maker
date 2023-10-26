package com.project.playlistmaker.favourite.data.impl

import com.project.playlistmaker.favourite.data.db.database.FavouritesDataBase
import com.project.playlistmaker.favourite.data.db.entity.FavouriteEntity
import com.project.playlistmaker.favourite.data.db.mappers.FavouriteDbMapper
import com.project.playlistmaker.favourite.domain.repository.FavouriteTracksRepository
import com.project.playlistmaker.searchscreen.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavouriteTracksRepositoryImpl(
    private val favouritesDataBase: FavouritesDataBase,
    private val trackDbConvertor: FavouriteDbMapper,
) : FavouriteTracksRepository {

    override suspend fun addToFavorites(track: Track) {
        favouritesDataBase.favoritesDao()
            .addToFavorites(trackDbConvertor.mapTrackToTrackEntity(track))
    }

    override suspend fun getFavoritesTracks(): Flow<List<Track>> = flow {
        val tracks = favouritesDataBase.favoritesDao().getTracks()
        emit(convertFromTrackEntity(tracks))
    }

    override suspend fun deleteFromFavorites(trackId: Long?) {
        favouritesDataBase.favoritesDao().deleteFromFavorites(trackId)
    }

    override suspend fun isFavoriteTrack(trackId: Long?): Flow<Boolean> = flow {
        val isFavorite = favouritesDataBase.favoritesDao().isFavoriteTrack(trackId)
        emit(isFavorite)
    }

    private fun convertFromTrackEntity(tracks: List<FavouriteEntity>): List<Track> {
        return tracks.map { track -> trackDbConvertor.mapTrackEntityToTrack(track) }
    }
}