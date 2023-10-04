package com.project.playlistmaker.mediascreen.favourite.data.impl

import com.project.playlistmaker.mediascreen.favourite.data.database.AppDatabase
import com.project.playlistmaker.mediascreen.favourite.data.entity.TrackEntity
import com.project.playlistmaker.mediascreen.favourite.data.mapper.TrackDbMapper
import com.project.playlistmaker.mediascreen.favourite.domain.repository.FavouriteTracksRepository
import com.project.playlistmaker.searchscreen.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavouriteTracksRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbMapper,
) : FavouriteTracksRepository {


    override suspend fun addToFavorites(track: Track) {
        appDatabase.TrackDao().addToFavorites(trackDbConvertor.mapTrackToTrackEntity(track))
    }

    override suspend fun getFavoritesTracks(): Flow<List<Track>> = flow {
        val tracks = appDatabase.TrackDao().getTracks()
        emit(convertFromTrackEntity(tracks))
    }

    override suspend fun deleteFromFavorites(trackId: Int) {
        appDatabase.TrackDao().deleteFromFavorites(trackId)
    }

    override suspend fun isFavoriteTrack(trackId: Int): Flow<Boolean> = flow {
        val isFavorite = appDatabase.TrackDao().isFavoriteTrack(trackId)
        emit(isFavorite)
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> trackDbConvertor.mapTrackEntityToTrack(track) }
    }
}