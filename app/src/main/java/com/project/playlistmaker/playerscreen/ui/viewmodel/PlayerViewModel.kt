package com.project.playlistmaker.playerscreen.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.playlistmaker.favourite.domain.interactor.FavouriteTracksInteractor
import com.project.playlistmaker.playerscreen.domain.playerinteractor.PlayerInteractor
import com.project.playlistmaker.playerscreen.ui.model.playerstate.PlayerState
import com.project.playlistmaker.searchscreen.domain.models.Track
import com.project.playlistmaker.utils.DataFormat
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val playerInteractor: PlayerInteractor,
    private val favTracksInteractor: FavouriteTracksInteractor
) : ViewModel() {

    //Observers
    private val _state = MutableLiveData<PlayerState>()
    fun observePlayerState(): LiveData<PlayerState> = _state

    //Current time livedata
    private val _duration = MutableLiveData<String>()
    fun observeCurrentDuration(): LiveData<String> = _duration

    private val isFavouriteLiveData = MutableLiveData<Boolean>()

    init {
        _state.postValue(PlayerState.STATE_DEFAULT)
        _duration.postValue("00:00")
    }

    //Timer variables
    private val dataFormat = DataFormat()
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
                    _duration.value = if (remainingTime > 0) {
                        dataFormat.roundTimeToMinAndSecond(remainingTime)
                    } else {
                        "00:00"
                    }
                }
            }
        }
    }

    //For addToFavourite
    fun observeFavourite(): LiveData<Boolean> = isFavouriteLiveData

    fun checkIsFavourite(trackId: Int) {
        viewModelScope.launch {
            favTracksInteractor
                .isFavoriteTrack(trackId)
                .collect { isFavourite ->
                    isFavouriteLiveData.postValue(isFavourite)
                }
        }
    }

    fun onFavouriteClicked(track: Track) {
        viewModelScope.launch {
            if (isFavouriteLiveData.value!!) {
                favTracksInteractor.deleteFromFavorites(track.trackId)
                isFavouriteLiveData.postValue(false)
            } else {
                favTracksInteractor.addToFavorites(track)
                isFavouriteLiveData.postValue(true)
            }
        }
    }

    companion object {
        //Count value for timer
        private const val UPDATE_DURATION_TIME_MILLIS = 300L
    }
}