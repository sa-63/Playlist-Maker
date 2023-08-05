package com.project.playlistmaker.player_screen.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.playlistmaker.creator.Creator
import com.project.playlistmaker.player_screen.domain.player_interactor.PlayerInteractor
import com.project.playlistmaker.player_screen.domain.player_state.PlayerState

class ActivityPlayerViewModel(private val playerInteractor: PlayerInteractor) : ViewModel() {

    private val playerStateLiveData = MutableLiveData<PlayerState>()
    fun observePlayerState(): LiveData<PlayerState> = playerStateLiveData

    init {
        playerStateLiveData.postValue(PlayerState.STATE_DEFAULT)
    }

    fun startAfterPrepare(afterPrepared: () -> Unit) {
        playerInteractor.startAfterPrepare(afterPrepared)
        playerStateLiveData.value = PlayerState.STATE_PLAYING
    }

    fun preparePlayer(url: String) {
        playerInteractor.preparePlayer(url)
        playerStateLiveData.value = PlayerState.STATE_PREPARED
    }

    fun releasePlayer() {
        playerInteractor.releasePlayer()
        playerStateLiveData.value = PlayerState.STATE_DEFAULT
    }

    fun pausePlayer() {
        playerInteractor.pausePlayer()
        playerStateLiveData.value = PlayerState.STATE_PAUSED
    }

    fun startPlayer() {
        playerInteractor.startPlayer()
        playerStateLiveData.value = PlayerState.STATE_PLAYING
    }

    fun getPlayerState(): PlayerState {
        return playerStateLiveData.value!!
    }

    fun setOnCompletionListener(whenComplete: () -> Unit) {
        playerInteractor.setOnCompletionListener(whenComplete)
    }

    fun getDuration(): Int {
        return playerInteractor.getDuration()
    }

    fun getCurrentPosition(): Int {
        return playerInteractor.getCurrentPosition()
    }

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ActivityPlayerViewModel (
                        Creator.providePlayerInteractor()
                    ) as T
                }
            }
    }
}