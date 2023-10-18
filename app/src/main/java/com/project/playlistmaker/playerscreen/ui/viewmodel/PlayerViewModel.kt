package com.project.playlistmaker.playerscreen.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.playlistmaker.favourite.domain.interactor.FavouriteTracksInteractor
import com.project.playlistmaker.playerscreen.domain.playerinteractor.PlayerInteractor
import com.project.playlistmaker.playerscreen.ui.model.PlayerState
import com.project.playlistmaker.playerscreen.ui.model.ToastState
import com.project.playlistmaker.playlist.domain.PlaylistInteractor
import com.project.playlistmaker.playlist.domain.models.states.EmptyStatePlaylist
import com.project.playlistmaker.playlist.domain.models.states.StateAddDb
import com.project.playlistmaker.playlist.domain.models.states.entity.Playlist
import com.project.playlistmaker.playlist.ui.model.TrackPlr
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
    private val playlistInteractor: PlaylistInteractor
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
    private val emptyPlaylistLiveData = MutableLiveData<EmptyStatePlaylist>()
    fun getEmptyPlaylistLiveData(): LiveData<EmptyStatePlaylist> = emptyPlaylistLiveData

    private val addPlaylistLivaData = MutableLiveData<StateAddDb>()
    fun getAddPlaylistLivaData(): LiveData<StateAddDb> = addPlaylistLivaData


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

    private fun preparePlayer(url: String?) {
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

    fun playbackControl(trackUrl: String?) {
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

    fun checkIsFavourite(trackId: Long?) {
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
    private fun renderPlaylistState(state: EmptyStatePlaylist) {
        emptyPlaylistLiveData.postValue(state)
    }

    private fun renderAddTrackState(state: StateAddDb){
        addPlaylistLivaData.postValue(state)
    }

    fun getAllPlaylists() {
        viewModelScope.launch {
            playlistInteractor.getAllPlaylists().collect {
                when (it) {
                    is EmptyStatePlaylist.EmptyPlaylist -> renderPlaylistState(it)
                    is EmptyStatePlaylist.NotEmptyPlaylist -> renderPlaylistState(it)
                }
            }
        }
    }

    fun addTrackInPlaylist(track: TrackPlr, playlist: Playlist) {

        if (track.trackId == null) {
            renderAddTrackState(StateAddDb.Error())
            return
        }

        viewModelScope.launch {

            if (playlist.tracksInPlaylist == null) {
                val stateError = playlistInteractor.addTrackInPlaylist(
                    TrackPlr.mappingTrack(track),
                    playlist.id!!
                )
                when (stateError) {
                    is StateAddDb.Error -> renderAddTrackState(StateAddDb.Error())
                    is StateAddDb.NoError -> renderAddTrackState(StateAddDb.NoError(playlist.playlistName))
                    is StateAddDb.Match -> renderAddTrackState(StateAddDb.Error())
                    is StateAddDb.NoData -> renderAddTrackState(StateAddDb.NoData())
                }
            } else {

                if (playlist.tracksInPlaylist.contains(track.trackId!!)) {
                    renderAddTrackState(StateAddDb.Match(playlist.playlistName))
                } else {
                    val stateError = playlistInteractor.addTrackInPlaylist(
                        TrackPlr.mappingTrack(track),
                        playlist.id!!
                    )
                    when (stateError) {
                        is StateAddDb.Error -> renderAddTrackState(StateAddDb.Error())
                        is StateAddDb.NoError -> renderAddTrackState(StateAddDb.NoError(playlist.playlistName))
                        is StateAddDb.Match -> renderAddTrackState(StateAddDb.Error())
                        is StateAddDb.NoData -> renderAddTrackState(StateAddDb.NoData())
                    }
                }
            }
        }
    }

    fun setStateNoDataPlaylistLivaData(){
        renderAddTrackState(StateAddDb.NoData())
    }

    companion object {
        //Count value for timer
        private const val UPDATE_DURATION_TIME_MILLIS = 300L
    }
}