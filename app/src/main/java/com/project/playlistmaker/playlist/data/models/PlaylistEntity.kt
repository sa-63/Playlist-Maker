package com.project.playlistmaker.playlist.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name ="id")
    val id:Int? = null,
    @ColumnInfo(name = "playlistName")
    val playlistName: String? = null,
    @ColumnInfo(name = "descriptionPlaylist")
    val descriptionPlaylist: String? = null,
    @ColumnInfo(name = "imageInStorage")
    val imageInStorage: String? = null,
    @ColumnInfo(name = "tracksInPlaylist")
    val tracksInPlaylist: String? = null,
    @ColumnInfo(name = "countTracks")
    val countTracks: Int = 0
    )
