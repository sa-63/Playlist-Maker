package com.project.playlistmaker.playlist.data.impl

import android.annotation.SuppressLint
import com.project.playlistmaker.playlist.data.db.database.PlaylistDataBase
import com.project.playlistmaker.playlist.data.db.entity.PlaylistEntity
import com.project.playlistmaker.playlist.data.db.entity.TrackEntityInPlaylist
import com.project.playlistmaker.playlist.data.storage.PlaylistStorage
import com.project.playlistmaker.playlist.domain.PlaylistRepository
import com.project.playlistmaker.playlist.domain.models.entity.Playlist
import com.project.playlistmaker.searchscreen.domain.models.Track
import com.project.playlistmaker.utils.Mapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class PlaylistRepositoryImpl(
    private val database: PlaylistDataBase,
    private val storage: PlaylistStorage
) : PlaylistRepository {

    override suspend fun addPlaylist(playlist: Playlist): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                database.getPlaylistDao().insertPlaylist(
                    Mapper.getPlaylistEntityFromPlaylist(playlist)
                )
                false
            } catch (exp: Throwable) {
                true
            }
        }
    }

    override suspend fun getAllPlaylists(): Flow<List<Playlist>?> {
        return flow {
            emit(withContext(Dispatchers.IO) {
                val playlists = database.getPlaylistDao().getAllPlaylists()

                if (playlists != null) {
                    Mapper.getArrayPlaylistFromPlaylistEntity(playlists)
                } else {
                    null
                }
            })
        }
    }

    override suspend fun addTrackInPlaylist(
        track: Track,
        idPlaylist: Int
    ): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                insertTrackInCommonTable(track)
                insertTrackInPlaylist(track, idPlaylist)

                false
            } catch (exp: Throwable) {
                true
            }
        }
    }

    private suspend fun removeTrackFromCommonTable(idTrack: Long) {
        val allPlaylists = database.getPlaylistDao().getAllPlaylists()

        allPlaylists!!.map {
            val listId = Mapper.takeFromJson(it.tracksInPlaylist)

            if (listId.contains(idTrack)) {
                return
            }
        }

        database.getPlaylistDao().deleteTrackInCommonTable(idTrack)
    }

    private suspend fun insertTrackInCommonTable(track: Track) {
        database.getPlaylistDao().insertTrackInCommonTable(
            Mapper.getTrackEntityFromTrack(track)
        )
    }

    private suspend fun insertTrackInPlaylist(track: Track, idPlaylist: Int) {
        val playlist = database.getPlaylistDao().getPlaylist(idPlaylist)
        val listId = Mapper.takeFromJson(playlist.tracksInPlaylist)

        listId.add(track.trackId!!)

        val newModifiedPlaylist = PlaylistEntity(
            id = playlist.id,
            playlistName = playlist.playlistName,
            descriptionPlaylist = playlist.descriptionPlaylist,
            imageInStorage = playlist.imageInStorage,
            tracksInPlaylist = Mapper.toJsonFromArray(listId),
            countTracks = playlist.countTracks + ONE_TRACK

        )

        database.getPlaylistDao().updatePlaylist(newModifiedPlaylist)
    }

    private suspend fun removeTrackFromPlDb(idPlaylist: Int, idTrack: Long) {
        val playlist = database.getPlaylistDao().getPlaylist(idPlaylist)
        val listId = Mapper.takeFromJson(playlist.tracksInPlaylist)

        listId.remove(idTrack)

        val newModifiedPlaylist = PlaylistEntity(
            id = playlist.id,
            playlistName = playlist.playlistName,
            descriptionPlaylist = playlist.descriptionPlaylist,
            imageInStorage = playlist.imageInStorage,
            tracksInPlaylist = Mapper.toJsonFromArray(listId),
            countTracks = playlist.countTracks - ONE_TRACK
        )

        database.getPlaylistDao().insertPlaylist(newModifiedPlaylist)
    }

    //StN
    override suspend fun getTracksFromCommonTable(listIdTracks: ArrayList<Long>): Flow<List<Track>?> {
        return flow {
            emit(withContext(Dispatchers.IO) {
                if (listIdTracks.isEmpty()) {
                    null
                } else {
                    val tracksList: ArrayList<TrackEntityInPlaylist> = arrayListOf()

                    for (i in 0 until listIdTracks.size) {

                        val track: TrackEntityInPlaylist =
                            database.getPlaylistDao().getTrackFromCommonTable(listIdTracks[i])

                        tracksList.add(track)
                    }
                    Mapper.getArrayTrackFromTrackEntityPlaylist(tracksList)
                }
            })
        }
    }


    override suspend fun deleteTrackFromPlaylist(
        idPlaylist: Int,
        idTrack: Long
    ): Flow<List<Track>?>? {
        try {
            removeTrackFromPlDb(idPlaylist, idTrack)
            removeTrackFromCommonTable(idTrack)

            val playlist = database.getPlaylistDao().getPlaylist(idPlaylist)
            val listId = Mapper.takeFromJson(playlist.tracksInPlaylist)

            return if (listId.isEmpty()) {
                flow { emit(ArrayList<Track>()) }
            } else {
                getTracksFromCommonTable(listId)
            }
        } catch (exp: Throwable) {
            return null
        }
    }

    @SuppressLint("SuspiciousIndentation")
    override suspend fun deletePlaylist(idPlaylist: Int): Flow<Unit?> {
        try {
            val playlist = database.getPlaylistDao().getPlaylist(idPlaylist)
            val listId = Mapper.takeFromJson(playlist.tracksInPlaylist)
            if (listId.isEmpty()) {
                database.getPlaylistDao().deletePlaylist(idPlaylist)
                return flow { emit(Unit) }
            } else {
                database.getPlaylistDao().deletePlaylist(idPlaylist)
                for (i in 0 until listId.size) {
                    removeTrackFromCommonTable(listId[i])
                }
                return flow { emit(Unit) }
            }

        } catch (ext: Throwable) {
            return flow { emit(null) }
        }
    }

    override suspend fun getPlaylist(idPlaylist: Int): Flow<Playlist?> {
        try {
            val playlist = database.getPlaylistDao().getPlaylist(idPlaylist)

            return flow {
                emit(
                    Mapper.getPlaylistFromPlaylistEntity(playlist)
                )
            }

        } catch (exp: Throwable) {
            return flow { emit(null) }
        }
    }

    override suspend fun updatePlaylist(
        idPlaylist: Int,
        namePlaylist: String?,
        descriptionPlaylist: String?,
        imagePlaylist: String?
    ): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val playlist = database.getPlaylistDao().getPlaylist(idPlaylist)
                database.getPlaylistDao().updatePlaylist(
                    PlaylistEntity(
                        id = idPlaylist,
                        playlistName = namePlaylist,
                        descriptionPlaylist = descriptionPlaylist,
                        imageInStorage = getSavedImageFromPrivateStorage(imagePlaylist),
                        tracksInPlaylist = playlist.tracksInPlaylist,
                        countTracks = playlist.countTracks
                    )
                )
                false

            } catch (exp: Throwable) {
                true
            }
        }
    }

    override fun getSavedImageFromPrivateStorage(uriFile: String?): String? {
        return storage.getSavedImageFromPrivateStorage(uriFile)
    }

    companion object {
        private const val ONE_TRACK = 1
    }
}