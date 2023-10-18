package com.project.playlistmaker.db.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.project.playlistmaker.db.dao.TrackDao
import com.project.playlistmaker.db.entity.TrackEntity

@Database(
    version = 1, entities = [TrackEntity::class], exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoritesDao(): TrackDao
}

