package com.project.playlistmaker.playlist.domain

import com.project.playlistmaker.playlist.domain.models.entity.Playlist
import com.project.playlistmaker.playlist.domain.models.states.EmptyStatePlaylist
import com.project.playlistmaker.playlist.domain.models.states.StateAddDb
import com.project.playlistmaker.playlist.domain.models.states.StateTracksInPlaylist
import com.project.playlistmaker.searchscreen.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    fun getSavedImageFromPrivateStorage(uriFile: String?): String?

    suspend fun addPlaylist(myPlaylist: Playlist): StateAddDb

    suspend fun addTrackInPlaylist(track: Track, idPlaylist: Int): StateAddDb

    suspend fun getAllPlaylists(): Flow<EmptyStatePlaylist>

    suspend fun getTracksFromCommonTable(listIdTracks: ArrayList<Long>): Flow<StateTracksInPlaylist>

    suspend fun deleteTrackFromPlaylist(idPlaylist: Int, idTrack: Long): Flow<StateTracksInPlaylist>

    suspend fun deletePlaylist(idPlaylist: Int): Flow<StateTracksInPlaylist>

    suspend fun getPlaylist(idPlaylist: Int): Flow<StateTracksInPlaylist>

    suspend fun updatePlaylist(
        idPlaylist: Int,
        namePlaylist: String?,
        descriptionPlaylist: String?,
        imagePlaylist: String?
    ): StateAddDb
}