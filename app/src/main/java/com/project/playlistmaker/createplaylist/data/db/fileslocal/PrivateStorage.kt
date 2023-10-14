package com.project.playlistmaker.createplaylist.data.db.fileslocal

import android.net.Uri
import java.net.URI

interface PrivateStorage {
    suspend fun saveImage(uri: Uri): URI
}