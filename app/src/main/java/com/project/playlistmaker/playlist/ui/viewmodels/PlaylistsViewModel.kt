package com.project.playlistmaker.playlist.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.playlistmaker.playlist.domain.PlaylistInteractor
import com.project.playlistmaker.playlist.domain.models.states.EmptyStatePlaylist
import kotlinx.coroutines.launch

class PlaylistsViewModel(private val playlistInteractor: PlaylistInteractor) : ViewModel() {

    private val emptyStateLiveData = MutableLiveData<EmptyStatePlaylist>()
    fun getEmptyStateLiveData(): LiveData<EmptyStatePlaylist> = emptyStateLiveData

    fun getAllPlaylists() {
        viewModelScope.launch {
            playlistInteractor.getAllPlaylists().collect {
                when (it) {
                    is EmptyStatePlaylist.EmptyPlaylist -> renderState(it)
                    is EmptyStatePlaylist.NotEmptyPlaylist -> renderState(it)
                }
            }
        }
    }

    private fun renderState(state: EmptyStatePlaylist) {
        emptyStateLiveData.postValue(state)
    }
}