package com.project.playlistmaker.db.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.project.playlistmaker.createplaylist.data.db.dao.PlaylistTracksCrossRefDao
import com.project.playlistmaker.createplaylist.data.db.dao.TracksInPlDao
import com.project.playlistmaker.createplaylist.data.db.entity.PlaylistTracksCrossRef
import com.project.playlistmaker.createplaylist.data.db.entity.TrackInPlEntity
import com.project.playlistmaker.db.dao.PlaylistsDao
import com.project.playlistmaker.db.dao.TrackDao
import com.project.playlistmaker.db.entity.PlaylistEntity
import com.project.playlistmaker.db.entity.TrackEntity

@Database(
    version = 1, entities = [TrackEntity::class,
        PlaylistEntity::class,
        TrackInPlEntity::class,
        PlaylistTracksCrossRef::class], exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoritesDao(): TrackDao
    abstract fun playlistsDao(): PlaylistsDao
    abstract fun tracksInPlDao(): TracksInPlDao
    abstract fun playlistsTracksCrossRefDao(): PlaylistTracksCrossRefDao
}

