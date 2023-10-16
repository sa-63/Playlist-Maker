package com.project.playlistmaker.createplaylist.domain.impl

import android.net.Uri
import com.project.playlistmaker.createplaylist.domain.interactor.PlaylistsFilesInteractor
import com.project.playlistmaker.createplaylist.domain.repository.PlaylistsFilesRepository
import java.net.URI

class PlaylistsFilesInteractorImpl(private val filesRepository: PlaylistsFilesRepository) :
    PlaylistsFilesInteractor {
    override suspend fun addToPrivateStorage(uri: Uri): URI {
        return filesRepository.addToPrivateStorage(uri)
    }
}