package com.project.playlistmaker.mediascreen.favourite.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.project.playlistmaker.mediascreen.favourite.data.dao.TrackDao
import com.project.playlistmaker.mediascreen.favourite.data.entity.TrackEntity

@Database(
    version = 1, entities = [TrackEntity::class]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun TrackDao(): TrackDao
}