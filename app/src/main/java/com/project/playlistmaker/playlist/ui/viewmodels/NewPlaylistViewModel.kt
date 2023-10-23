package com.project.playlistmaker.playlist.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.playlistmaker.playlist.domain.PlaylistInteractor
import com.project.playlistmaker.playlist.domain.models.states.StateAddDb
import com.project.playlistmaker.playlist.domain.models.states.entity.Playlist
import kotlinx.coroutines.launch

open class NewPlaylistViewModel(private val playlistInteractor: PlaylistInteractor) : ViewModel() {

    private val stateLiveData = MutableLiveData<StateAddDb>()
    fun getLiveData(): LiveData<StateAddDb> = stateLiveData

    fun addPlaylist(playlist: Playlist) {
        playlist.imageInStorage = playlistInteractor.getSavedImageFromPrivateStorage(playlist.imageInStorage)
        viewModelScope.launch {
            val result = playlistInteractor.addPlaylist(playlist)

            when (result) {
                is StateAddDb.Error -> renderState(result)
                is StateAddDb.NoError -> renderState(result)
                is StateAddDb.Match -> renderState(result)
                is StateAddDb.NoData -> renderState(result)
            }
        }
    }

    fun updatePlaylist(
        idPlaylist: Int,
        namePlaylist: String?,
        descriptionPlaylist: String?,
        imagePlaylist: String?
    ) {
        viewModelScope.launch {
            val result = playlistInteractor.updatePlaylist(
                idPlaylist,
                namePlaylist,
                descriptionPlaylist,
                imagePlaylist
            )
            renderState(result)
        }
    }

    private fun renderState(state: StateAddDb) {
        stateLiveData.postValue(state)
    }
}