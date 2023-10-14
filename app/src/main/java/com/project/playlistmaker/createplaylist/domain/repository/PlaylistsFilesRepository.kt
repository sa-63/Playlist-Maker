package com.project.playlistmaker.createplaylist.domain.repository

import android.net.Uri
import java.net.URI

interface PlaylistsFilesRepository {
    suspend fun addToPrivateStorage(uri: Uri): URI
}