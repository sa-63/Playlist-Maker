package com.project.playlistmaker.createplaylist.data.db.fileslocal

import android.net.Uri
import com.project.playlistmaker.createplaylist.domain.repository.PlaylistsFilesRepository
import java.net.URI

class PlaylistsFilesRepositoryImpl(private val privateStorage: PrivateStorage) :
    PlaylistsFilesRepository {
    override suspend fun addToPrivateStorage(uri: Uri): URI {
        return privateStorage.saveImage(uri)
    }
}