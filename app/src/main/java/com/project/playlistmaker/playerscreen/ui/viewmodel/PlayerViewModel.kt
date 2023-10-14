package com.project.playlistmaker.playerscreen.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.playlistmaker.R
import com.project.playlistmaker.createplaylist.domain.interactor.PlaylistDbInteractor
import com.project.playlistmaker.createplaylist.domain.model.Playlist
import com.project.playlistmaker.favourite.domain.interactor.FavouriteTracksInteractor
import com.project.playlistmaker.playerscreen.domain.playerinteractor.PlayerInteractor
import com.project.playlistmaker.playerscreen.ui.model.PlayerState
import com.project.playlistmaker.playerscreen.ui.model.PlaylistsInPlayerState
import com.project.playlistmaker.playerscreen.ui.model.ToastState
import com.project.playlistmaker.searchscreen.domain.models.Track
import com.project.playlistmaker.utils.DataFormat
import com.project.playlistmaker.utils.ResourceProvider
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val track: Track,
    private val playerInteractor: PlayerInteractor,
    private val resourceProvider: ResourceProvider,
    private val favTracksInteractor: FavouriteTracksInteractor,
    private val playlistsDbInteractor: PlaylistDbInteractor
) : ViewModel() {


    //Observers
    private val _state = MutableLiveData<PlayerState>()
    fun observePlayerState(): LiveData<PlayerState> = _state

    //Current time livedata
    private val _duration = MutableLiveData<String>()
    fun observeCurrentDuration(): LiveData<String> = _duration

    //Favourite
    private val isFavouriteLiveData = MutableLiveData<Boolean>()

    //Toast
    private val toastLiveData = MutableLiveData<ToastState>()
    fun observeToastState(): LiveData<ToastState> = toastLiveData

    //Playlists
    private val playlistsLiveData = MutableLiveData<PlaylistsInPlayerState>()
    fun observePlaylists(): LiveData<PlaylistsInPlayerState> = playlistsLiveData

    init {
        _state.postValue(PlayerState.STATE_DEFAULT)
        _duration.postValue("00:00")
    }

    //Timer variables
    private val dataFormat = DataFormat()
    private var timerJob: Job? = null

    //FavTracks
    private var isFavourite = false

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
                .collect { isFavorite ->
                    isFavourite = isFavorite
                    isFavouriteLiveData.postValue(isFavourite)
                }
        }
    }

    fun onFavouriteClicked(track: Track) {
        viewModelScope.launch {
            isFavourite = if (isFavourite) {
                favTracksInteractor.deleteFromFavorites(track.trackId)
                isFavouriteLiveData.postValue(false)
                false
            } else {
                favTracksInteractor.addToFavorites(track)
                isFavouriteLiveData.postValue(true)
                true
            }
        }
    }

    //Toast Related
    fun toastWasShown() {
        toastLiveData.value = ToastState.None
    }

    private fun showToast(message: String) {
        toastLiveData.value =
            ToastState.Show(message)
    }

    //Playlists Related
    fun addToPlaylistClicked() {
        viewModelScope.launch {
            playlistsDbInteractor.getPlaylists().collect {
                playlistsLiveData.postValue(PlaylistsInPlayerState.DisplayPlaylists(it))
            }
        }
    }

    fun onPlaylistClicked(playlist: Playlist) {
        if (playlist.tracks.contains(track.trackId)) {
            showToast("${resourceProvider.getString(R.string.track_is_in_pl_already)} ${playlist.name}")
        } else {
            viewModelScope.launch {
                playlist.tracks.add(track.trackId)
                val updatedPlaylist = playlist.copy(numberOfTracks = playlist.numberOfTracks + 1)
                playlistsDbInteractor.addTrackToPlaylist(track, updatedPlaylist)
            }
            showToast("${resourceProvider.getString(R.string.track_added_to_pl)} ${playlist.name}")
        }
        playlistsLiveData.postValue(PlaylistsInPlayerState.HidePlaylists)
    }

    companion object {
        //Count value for timer
        private const val UPDATE_DURATION_TIME_MILLIS = 300L
    }
}