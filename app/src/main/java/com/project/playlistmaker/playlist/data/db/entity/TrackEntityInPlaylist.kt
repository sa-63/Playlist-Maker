package com.project.playlistmaker.playlist.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tracks_in_playlists")

data class TrackEntityInPlaylist(
    @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "trackId")
    var trackId: Long? = null,
    @ColumnInfo(name = "isFavorite")
    var isFavorite: Boolean = false,
    @ColumnInfo(name = "trackName")
    var trackName: String? = null,
    @ColumnInfo(name = "artistName")
    var artistName: String? = null,
    @ColumnInfo(name = "trackTimeMillis")
    var trackTimeMillis: Long? = null,
    @ColumnInfo(name = "artworkUrl100")
    var artworkUrl100: String? = null,
    @ColumnInfo(name = "collectionName")
    var collectionName: String? = null,
    @ColumnInfo(name = "releaseDate")
    var releaseDate: String? = null,
    @ColumnInfo(name = "primaryGenreName")
    var primaryGenreName: String? = null,
    @ColumnInfo(name = "country")
    var country: String? = null,
    @ColumnInfo(name = "previewUrl")
    var previewUrl: String? = null
)