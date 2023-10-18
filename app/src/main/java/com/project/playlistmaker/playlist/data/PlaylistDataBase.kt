package com.project.playlistmaker.playlist.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.project.playlistmaker.playlist.data.dao.PlaylistDao
import com.project.playlistmaker.playlist.data.models.PlaylistEntity
import com.project.playlistmaker.playlist.data.models.TrackEntityInPlaylist

@Database(version = 1, entities = [PlaylistEntity::class, TrackEntityInPlaylist::class])
abstract class PlaylistDataBase: RoomDatabase() {

    abstract fun getPlaylistDao(): PlaylistDao
}