package com.project.playlistmaker.createplaylist.domain.interactor

import android.net.Uri
import java.net.URI

interface PlaylistsFilesInteractor {
    suspend fun addToPrivateStorage(uri: Uri): URI
}