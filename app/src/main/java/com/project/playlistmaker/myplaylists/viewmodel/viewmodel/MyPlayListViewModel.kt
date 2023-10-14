package com.project.playlistmaker.myplaylists.viewmodel.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.playlistmaker.createplaylist.domain.interactor.PlaylistDbInteractor
import com.project.playlistmaker.createplaylist.domain.model.Playlist
import com.project.playlistmaker.myplaylists.viewmodel.models.PlaylistState
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MyPlayListViewModel(private val interactor: PlaylistDbInteractor) : ViewModel() {

    //LiveData
    private val playlistsLiveData = MutableLiveData<PlaylistState>()
    fun observePlaylists(): LiveData<PlaylistState> = playlistsLiveData

    //Debounce
    private var isClickAllowed = true
    private var clickDebounceJob: Job? = null

    fun displayState() {
        fillData()
    }

    private fun fillData() {
        viewModelScope.launch {
            interactor.getPlaylists().collect {
                processResult(it)
            }
        }
    }

    private fun processResult(playlists: List<Playlist>) {
        if (playlists.isEmpty()) playlistsLiveData.postValue(PlaylistState.DisplayEmptyPlaylists)
        else playlistsLiveData.postValue(PlaylistState.DisplayPlaylists(playlists))
    }

    fun clickDebounce(): Boolean {
        val currentState = isClickAllowed
        clickDebounceJob?.cancel()
        if (isClickAllowed) {
            //To Do
        }
        return currentState
    }
}