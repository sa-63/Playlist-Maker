package com.project.playlistmaker.playlist.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.playlistmaker.playerscreen.ui.model.ToastState
import com.project.playlistmaker.playlist.domain.PlaylistInteractor
import com.project.playlistmaker.playlist.domain.models.states.StateTracksInPlaylist
import com.project.playlistmaker.sharing.domain.interactor.SharingInteractor
import kotlinx.coroutines.launch

open class PlaylistTracksViewModel(
    private val playlistInteractor: PlaylistInteractor,
    private val sharingInteractor: SharingInteractor
) : ViewModel() {

    private val statePlaylistLiveData = MutableLiveData<StateTracksInPlaylist>()
    fun getStatePlaylistLiveData(): LiveData<StateTracksInPlaylist> = statePlaylistLiveData

    private val toastStateLiveData = MutableLiveData<ToastState>()
    fun getToastStateLiveData(): LiveData<ToastState> = toastStateLiveData

    fun getPlaylist(idPlaylist: Int) {
        viewModelScope.launch {
            playlistInteractor.getPlaylist(idPlaylist).collect {
                renderStateTracksInPlaylist(it)
            }
        }
    }

    fun getTracksFromCommonTable(listIdTracks: ArrayList<Long>) {
        viewModelScope.launch {
            playlistInteractor.getTracksFromCommonTable(listIdTracks).collect {
                renderStateTracksInPlaylist(it)
            }
        }
    }

    fun deleteTrackFromPlaylist(idPlaylist: Int, idTrack: Long) {
        viewModelScope.launch {
            playlistInteractor.deleteTrackFromPlaylist(idPlaylist, idTrack).collect {
                renderStateTracksInPlaylist(it)
            }
        }
    }

    fun deletePlaylist(idPlaylist: Int) {
        viewModelScope.launch {
            playlistInteractor.deletePlaylist(idPlaylist).collect {
                renderStateTracksInPlaylist(it)
            }
        }
    }

    fun showToast(message: String) {
        toastStateLiveData.postValue(ToastState.Show(message))
    }

    fun setStateToastNone() {
        toastStateLiveData.postValue(ToastState.None)
    }

    fun sharePlaylist(playlistInMessage: String) {
        sharingInteractor.sharePlaylist(playlistInMessage)
    }

    private fun renderStateTracksInPlaylist(state: StateTracksInPlaylist) {
        statePlaylistLiveData.postValue(state)
    }
}