package com.project.playlistmaker.playlist.domain


import com.project.playlistmaker.playlist.domain.models.states.entity.Playlist
import com.project.playlistmaker.searchscreen.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    fun getSavedImageFromPrivateStorage(uriFile: String?): String?

    suspend fun addPlaylist(playlist: Playlist): Boolean

    suspend fun addTrackInPlaylist(
        track: Track,
        idPlaylist: Int
    ): Boolean

    suspend fun getAllPlaylists(): Flow<List<Playlist>?>

    suspend fun getTracksFromCommonTable(listIdTracks: ArrayList<Long>): Flow<List<Track>?>

    suspend fun deleteTrackFromPlaylist(
        idPlaylist: Int,
        idTrack: Long,
    ): Flow<List<Track>?>?

    suspend fun deletePlaylist(idPlaylist: Int): Flow<Unit?>

    suspend fun getPlaylist(idPlaylist: Int): Flow<Playlist?>

    suspend fun updatePlaylist(
        idPlaylist: Int,
        namePlaylist: String?,
        descriptionPlaylist: String?,
        imagePlaylist: String?
    ) : Boolean
}