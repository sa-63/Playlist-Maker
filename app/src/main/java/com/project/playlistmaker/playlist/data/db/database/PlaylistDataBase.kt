package com.project.playlistmaker.playlist.data.db.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.project.playlistmaker.playlist.data.db.dao.PlaylistDao
import com.project.playlistmaker.playlist.data.db.entity.PlaylistEntity
import com.project.playlistmaker.playlist.data.db.entity.TrackEntityInPlaylist

@Database(version = 1, entities = [PlaylistEntity::class, TrackEntityInPlaylist::class])
abstract class PlaylistDataBase : RoomDatabase() {
    abstract fun getPlaylistDao(): PlaylistDao
}