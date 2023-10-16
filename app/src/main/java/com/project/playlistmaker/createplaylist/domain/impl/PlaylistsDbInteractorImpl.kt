package com.project.playlistmaker.createplaylist.domain.impl

import com.project.playlistmaker.createplaylist.domain.interactor.PlaylistDbInteractor
import com.project.playlistmaker.createplaylist.domain.model.Playlist
import com.project.playlistmaker.createplaylist.domain.repository.PlaylistsRepository
import com.project.playlistmaker.searchscreen.domain.models.Track
import kotlinx.coroutines.flow.Flow

class PlaylistsDbInteractorImpl(private val repository: PlaylistsRepository) :
    PlaylistDbInteractor {
    override suspend fun addPlaylist(playlist: Playlist) {
        repository.addPlaylist(playlist)
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        repository.deletePlaylist(playlist)
    }

    override fun getPlaylists(): Flow<List<Playlist>> = repository.getPlaylists()

    override suspend fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        repository.addTrackToPlaylist(track, playlist)
    }
}