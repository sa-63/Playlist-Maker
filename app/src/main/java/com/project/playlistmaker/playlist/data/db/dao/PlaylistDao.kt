package com.project.playlistmaker.playlist.data.db.dao

import androidx.room.*
import com.project.playlistmaker.playlist.data.db.entity.PlaylistEntity
import com.project.playlistmaker.playlist.data.db.entity.TrackEntityInPlaylist

@Dao
interface PlaylistDao {
    @Insert(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(track: PlaylistEntity)

    @Insert(entity = TrackEntityInPlaylist::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrackInCommonTable(track: TrackEntityInPlaylist)

    @Query("SELECT * FROM tracks_in_playlists WHERE trackId = :trackId")
    suspend fun getTrackFromCommonTable(trackId: Long): TrackEntityInPlaylist

    @Query("DELETE FROM tracks_in_playlists WHERE trackId = :trackId")
    suspend fun deleteTrackInCommonTable(trackId: Long)

    @Query("SELECT * FROM playlists WHERE id = :id")
    suspend fun getPlaylist(id: Int): PlaylistEntity

    @Query("DELETE FROM playlists WHERE id = :id")
    suspend fun deletePlaylist(id: Int)

    @Update(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePlaylist(playlist: PlaylistEntity)

    @Query("SELECT * FROM playlists")
    suspend fun getAllPlaylists(): List<PlaylistEntity>?
}