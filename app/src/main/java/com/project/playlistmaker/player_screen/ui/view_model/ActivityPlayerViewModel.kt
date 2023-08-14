package com.project.playlistmaker.player_screen.ui.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.playlistmaker.creator.Creator
import com.project.playlistmaker.player_screen.domain.player_interactor.PlayerInteractor
import com.project.playlistmaker.player_screen.ui.model.player_state.PlayerState
import com.project.playlistmaker.utils.DataFormat

class ActivityPlayerViewModel(
    private val playerInteractor: PlayerInteractor
) : ViewModel() {

    //Observers
    private val _state = MutableLiveData<PlayerState>()
    fun observePlayerState(): LiveData<PlayerState> = _state

    init {
        _state.postValue(PlayerState.STATE_DEFAULT)
    }

    companion object {
        //ViewModelProvider
        fun getViewModelFactory(): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ActivityPlayerViewModel(
                        Creator.providePlayerInteractor()
                    ) as T
                }
            }

        //Count value for timer
        private const val UPDATE_DURATION_TIME_MILLIS = 1000L
    }

    //Timer variables
    private val dataFormat = DataFormat()
    private  var currentTime = "00:00"
    private var handler = Handler(Looper.getMainLooper())

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
    }

    private fun pausePlayer() {
        playerInteractor.pausePlayer()
        removeCallbacks()
        _state.value = PlayerState.STATE_PAUSED
    }

    private fun startPlayer() {
        playerInteractor.startPlayer()
        startDurationTask()
        _state.value = PlayerState.STATE_PLAYING
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

    private val runnableTask = object : Runnable {
        override fun run() {
            if (_state.value == PlayerState.STATE_PLAYING) {
                _state.value = PlayerState.STATE_PLAYING
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
            handler.postDelayed(
                this,
                ActivityPlayerViewModel.UPDATE_DURATION_TIME_MILLIS
            )
        }
    }

    private fun removeCallbacks() {
        handler.removeCallbacks(runnableTask)
    }

    private fun startDurationTask() {
        handler.post(
            runnableTask
        )
    }

    fun getCurrentTrackDuration(): String {
        return currentTime
    }
}