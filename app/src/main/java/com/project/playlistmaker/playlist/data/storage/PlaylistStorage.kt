package com.project.playlistmaker.playlist.data.storage

interface PlaylistStorage {

    fun getSavedImageFromPrivateStorage (uriFile: String?): String?
}