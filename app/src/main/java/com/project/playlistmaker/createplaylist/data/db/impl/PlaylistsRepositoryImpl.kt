package com.project.playlistmaker.createplaylist.data.db.impl

import com.project.playlistmaker.createplaylist.data.db.entity.PlaylistTracksCrossRef
import com.project.playlistmaker.createplaylist.domain.model.Playlist
import com.project.playlistmaker.createplaylist.domain.repository.PlaylistsRepository
import com.project.playlistmaker.db.database.AppDatabase
import com.project.playlistmaker.db.entity.PlaylistEntity
import com.project.playlistmaker.db.mappers.PlaylistDbMapper
import com.project.playlistmaker.db.mappers.TrackDbMapper
import com.project.playlistmaker.searchscreen.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistsRepositoryImpl(
    private val database: AppDatabase,
    private val playlistDbMapper: PlaylistDbMapper,
    private val trackDbMapper: TrackDbMapper
) : PlaylistsRepository {

    override suspend fun addPlaylist(playlist: Playlist) {
        database.playlistsDao().insertPlaylist(playlistDbMapper.mapToPlaylistEntity(playlist))
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        database.playlistsDao().deletePlaylist(playlistDbMapper.mapToPlaylistEntity(playlist))
    }

    override fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = convertFromPlaylistEntity(database.playlistsDao().getPlaylists())
        for (playlist in playlists) {
            playlist.tracks.addAll(
                convertFromTracksInPlaylists(
                    database.playlistsTracksCrossRefDao().getTracksInPlaylist(playlist.playlistId)
                )
            )
        }
        emit(playlists)
    }

    override suspend fun addTrackToPlaylist(track: Track, playlist: Playlist) {
        database.playlistsDao().updateNumberOfTracks(
            playlist.playlistId, playlist.numberOfTracks
        )
        database.tracksInPlDao().addTrackToPl(trackDbMapper.mapTrackToTrackPl(track))
        database.playlistsTracksCrossRefDao()
            .addTrackInPl(PlaylistTracksCrossRef(track.trackId, playlist.playlistId))
    }

    private fun convertFromPlaylistEntity(playlists: List<PlaylistEntity>): List<Playlist> =
        playlists.map { playlist -> playlistDbMapper.mapToPlaylist(playlist) }

    private fun convertFromTracksInPlaylists(tracksInPlaylists: List<PlaylistTracksCrossRef>): List<Int> =
        tracksInPlaylists.map { track -> track.trackId }
}