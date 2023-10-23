package com.project.playlistmaker.playlist.domain.impl

import com.project.playlistmaker.playlist.domain.PlaylistInteractor
import com.project.playlistmaker.playlist.domain.PlaylistRepository
import com.project.playlistmaker.playlist.domain.models.states.EmptyStatePlaylist
import com.project.playlistmaker.playlist.domain.models.states.StateAddDb
import com.project.playlistmaker.playlist.domain.models.states.StateTracksInPlaylist
import com.project.playlistmaker.playlist.domain.models.states.entity.Playlist
import com.project.playlistmaker.searchscreen.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class PlaylistInteractorImpl(private val repository: PlaylistRepository) : PlaylistInteractor {

    override suspend fun addPlaylist(myPlaylist: Playlist): StateAddDb {
        return when (repository.addPlaylist(myPlaylist)) {
            false -> StateAddDb.NoError()
            else -> StateAddDb.Error()
        }
    }

    override suspend fun addTrackInPlaylist(track: Track, idPlaylist: Int): StateAddDb {
        return when (repository.addTrackInPlaylist(track, idPlaylist)) {
            false -> StateAddDb.NoError()
            else -> StateAddDb.Error()
        }
    }

    override suspend fun getAllPlaylists(): Flow<EmptyStatePlaylist> {
        return repository.getAllPlaylists().map { list ->
            if (list == null || list.isEmpty()) {
                EmptyStatePlaylist.EmptyPlaylist()
            } else {
                EmptyStatePlaylist.NotEmptyPlaylist(list)
            }
        }
    }

    override suspend fun getTracksFromCommonTable(listIdTracks: ArrayList<Long>): Flow<StateTracksInPlaylist> {
        return repository.getTracksFromCommonTable(listIdTracks).map {
            if (!it.isNullOrEmpty()) {
                var durationTimeSum: Long = 0
                for (i in 0 until it.size) {
                    durationTimeSum = durationTimeSum + (it[i].trackTimeMillis ?: 0)
                }
                StateTracksInPlaylist.WithTracks(it.asReversed(), durationTimeSum)
            } else {
                StateTracksInPlaylist.NoTracks()
            }
        }
    }

    override suspend fun deleteTrackFromPlaylist(
        idPlaylist: Int,
        idTrack: Long
    ): Flow<StateTracksInPlaylist> {
        val flowTrack = repository.deleteTrackFromPlaylist(idPlaylist, idTrack)

        if (flowTrack == null) {
            return flow { emit(StateTracksInPlaylist.ErrorStateTracks()) }
        }

        return flowTrack.map {
            if (it == null) {
                StateTracksInPlaylist.ErrorStateTracks()
            }
            if (it!!.isEmpty()) {
                StateTracksInPlaylist.NoTracks()
            } else {
                var durationTimeSum: Long = 0
                for (i in 0 until it.size) {
                    durationTimeSum = durationTimeSum + (it[i].trackTimeMillis ?: 0)
                }
                StateTracksInPlaylist.DeletedTrack(it.asReversed(), durationTimeSum, it.size)
            }
        }
    }

    override suspend fun deletePlaylist(idPlaylist: Int): Flow<StateTracksInPlaylist> {
        return repository.deletePlaylist(idPlaylist).map {
            when (it) {
                null -> StateTracksInPlaylist.ErrorStateTracks()
                else -> StateTracksInPlaylist.DeletedPlaylist()
            }
        }
    }

    override suspend fun getPlaylist(idPlaylist: Int): Flow<StateTracksInPlaylist> {
        return repository.getPlaylist(idPlaylist).map {
            when (it) {
                null -> StateTracksInPlaylist.ErrorStateTracks()
                else -> StateTracksInPlaylist.InitPlaylist(it)
            }
        }
    }

    override suspend fun updatePlaylist(
        idPlaylist: Int,
        namePlaylist: String?,
        descriptionPlaylist: String?,
        imagePlaylist: String?
    ): StateAddDb {
        return when (repository.updatePlaylist(
            idPlaylist,
            namePlaylist,
            descriptionPlaylist,
            imagePlaylist
        )) {
            false -> StateAddDb.NoError()
            else -> StateAddDb.Error()
        }
    }

    override fun getSavedImageFromPrivateStorage(uriFile: String?): String? {
        return repository.getSavedImageFromPrivateStorage(uriFile)
    }
}