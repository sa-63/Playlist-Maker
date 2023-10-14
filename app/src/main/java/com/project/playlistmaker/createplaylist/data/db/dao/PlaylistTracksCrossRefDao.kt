package com.project.playlistmaker.createplaylist.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.project.playlistmaker.createplaylist.data.db.entity.PlaylistTracksCrossRef

@Dao
interface PlaylistTracksCrossRefDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTrackInPl(trackInPlaylist: PlaylistTracksCrossRef)

    @Query("SELECT * FROM playlist_tracks_cross_ref_table WHERE playlistId = :playlistId")
    suspend fun getTracksInPlaylist(playlistId: Int): List<PlaylistTracksCrossRef>
}