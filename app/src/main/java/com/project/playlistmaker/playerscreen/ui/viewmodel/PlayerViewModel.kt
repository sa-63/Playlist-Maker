package com.project.playlistmaker.playerscreen.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.playlistmaker.playerscreen.domain.playerinteractor.PlayerInteractor
import com.project.playlistmaker.playerscreen.ui.model.playerstate.PlayerState
import com.project.playlistmaker.utils.DataFormat
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val playerInteractor: PlayerInteractor
) : ViewModel() {

    //Observers
    private val _state = MutableLiveData<PlayerState>()
    fun observePlayerState(): LiveData<PlayerState> = _state

    init {
        _state.postValue(PlayerState.STATE_DEFAULT)
    }

    //Timer variables
    private val dataFormat = DataFormat()
    private var currentTime = "00:00"
    private var timerJob: Job? = null


    private fun startAfterPrepare(afterPrepared: () -> Unit) {
        playerInteractor.startAfterPrepare(afterPrepared)
        _state.value = PlayerState.STATE_PLAYING
    }

    private fun preparePlayer(url: String) {
        playerInteractor.preparePlayer(url)
        _state.value = PlayerState.STATE_PREPARED
    }

    fun releasePlayer() {
        playerInteractor.releasePlayer()
        _state.value = PlayerState.STATE_DEFAULT
        timerJob?.cancel()
    }

    private fun pausePlayer() {
        playerInteractor.pausePlayer()
        _state.value = PlayerState.STATE_PAUSED
        timerJob?.cancel()
    }

    private fun startPlayer() {
        playerInteractor.startPlayer()
        _state.value = PlayerState.STATE_PLAYING
        startTimerJob()
    }

    private fun setOnCompletionListener(whenComplete: () -> Unit) {
        playerInteractor.setOnCompletionListener(whenComplete)
    }

    private fun getDuration(): Int {
        return playerInteractor.getDuration()
    }

    private fun getCurrentPosition(): Int {
        return playerInteractor.getCurrentPosition()
    }

    fun playbackControl(trackUrl: String) {
        when (_state.value!!) {
            PlayerState.STATE_PLAYING -> {
                pausePlayer()
            }

            PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED -> {
                startPlayer()
            }

            PlayerState.STATE_DEFAULT -> {
                preparePlayer(trackUrl)
                startAfterPrepare {
                    startPlayer()
                }
            }
        }

        setOnCompletionListener {
            pausePlayer()
        }
    }

    private fun startTimerJob() {
        timerJob = viewModelScope.launch {
            while (true) {
                if (_state.value == PlayerState.STATE_PLAYING) {
                    _state.value = PlayerState.STATE_PLAYING
                    delay(UPDATE_DURATION_TIME_MILLIS)
                    val remainingTime =
                        dataFormat.convertMediaPlayerRemainingTime(
                            getDuration(),
                            getCurrentPosition()
                        )
                    currentTime = if (remainingTime > 0) {
                        dataFormat.roundTimeToMinAndSecond(remainingTime)
                    } else {
                        "00:00"
                    }
                }
            }
        }
    }

    fun getCurrentTrackDuration(): String {
        return currentTime
    }

    companion object {
        //Count value for timer
        private const val UPDATE_DURATION_TIME_MILLIS = 1000L
    }
}