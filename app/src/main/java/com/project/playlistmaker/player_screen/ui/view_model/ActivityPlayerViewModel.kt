package com.project.playlistmaker.player_screen.ui.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.playlistmaker.creator.Creator
import com.project.playlistmaker.player_screen.domain.player_interactor.PlayerInteractor
import com.project.playlistmaker.player_screen.domain.player_state.PlayerState
import com.project.playlistmaker.use_cases.utilities.DataFormat

class ActivityPlayerViewModel(
    private val playerInteractor: PlayerInteractor
) : ViewModel() {

    //Observers
    private val playerStateLiveData = MutableLiveData<PlayerState>()
    fun observePlayerState(): LiveData<PlayerState> = playerStateLiveData

    private val durationTimeLiveData = MutableLiveData<String>()
    fun observeDurationTime(): LiveData<String> = durationTimeLiveData

    init {
        playerStateLiveData.postValue(PlayerState.STATE_DEFAULT)
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
    private var mainHandler: Handler = Handler(Looper.getMainLooper())

    private fun startAfterPrepare(afterPrepared: () -> Unit) {
        playerInteractor.startAfterPrepare(afterPrepared)
        playerStateLiveData.value = PlayerState.STATE_PLAYING
    }

    private fun preparePlayer(url: String) {
        playerInteractor.preparePlayer(url)
        playerStateLiveData.value = PlayerState.STATE_PREPARED
    }

    fun releasePlayer() {
        playerInteractor.releasePlayer()
        playerStateLiveData.value = PlayerState.STATE_DEFAULT
    }

    private fun pausePlayer() {
        playerInteractor.pausePlayer()
        removeCallbacks()
        playerStateLiveData.value = PlayerState.STATE_PAUSED
    }

    private fun startPlayer() {
        playerInteractor.startPlayer()
        startDurationTask()
        playerStateLiveData.value = PlayerState.STATE_PLAYING
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
        when (playerStateLiveData.value!!) {
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

    private fun createDurationCountTask(): Runnable {
        return object : Runnable {
            override fun run() {
                val remainingTime =
                    dataFormat.convertMediaPlayerRemainingTime(
                        getDuration(),
                        getCurrentPosition()
                    )
                if (remainingTime > 0) {
                    durationTimeLiveData.value = dataFormat.roundTimeToMinAndSecond(remainingTime)
                    mainHandler.postDelayed(this, UPDATE_DURATION_TIME_MILLIS)
                } else {
                    durationTimeLiveData.value = "00:00"
                }
            }
        }
    }

    private fun removeCallbacks() {
        mainHandler.removeCallbacks(createDurationCountTask())
    }

    private fun startDurationTask() {
        mainHandler.post(
            createDurationCountTask()
        )
    }
}