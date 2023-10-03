package com.project.playlistmaker.mediascreen.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.playlistmaker.mediascreen.domain.interactor.FavouriteTracksInteractor
import com.project.playlistmaker.mediascreen.ui.models.FavTracksState
import com.project.playlistmaker.searchscreen.ui.view_model.SearchViewModel.Companion.CLICK_DEBOUNCE_DELAY_MILLIS
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FavTracksViewModel(private val favouriteTracksInteractor: FavouriteTracksInteractor) :
    ViewModel() {

    //Debounce
    private var isClickAllowed = true
    private var clickDebounceJob: Job? = null

    //LiveData
    private val _state = MutableLiveData<FavTracksState>()
    fun observeState(): LiveData<FavTracksState> = _state

    fun getFavouriteTracks() {
        viewModelScope.launch {
            favouriteTracksInteractor
                .getFavoritesTracks()
                .collect { tracks ->
                    if (tracks.isEmpty()) {
                        setState(FavTracksState.Empty)
                    } else {
                        setState(FavTracksState.Content(tracks))
                    }
                }
        }
    }

    fun clickDebounce(): Boolean {
        val currentState = isClickAllowed
        clickDebounceJob?.cancel()
        if (isClickAllowed) {
            viewModelScope.launch {
                isClickAllowed = false
                delay(CLICK_DEBOUNCE_DELAY_MILLIS)
                isClickAllowed = true
            }
        }
        return currentState
    }

    private fun setState(state: FavTracksState) {
        _state.postValue(state)
    }
}