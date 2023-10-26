package com.project.playlistmaker.searchscreen.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.playlistmaker.searchscreen.domain.models.Track
import com.project.playlistmaker.searchscreen.domain.search_interactor.SearchInteractor
import com.project.playlistmaker.searchscreen.ui.models.SearchScreenStatus
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(private val searchInteractor: SearchInteractor) : ViewModel() {

    //For Debounce
    private var isClickAllowed = true
    private var clickDebounceJob: Job? = null
    private var searchDebounceJob: Job? = null

    //LiveData
    //Request status
    private val _state = MutableLiveData<SearchScreenStatus>()
    fun observeSearchStatusResultLiveData(): LiveData<SearchScreenStatus> = _state

    //History
    private val searchHistory = arrayListOf<Track>()

    fun showHistory() {
        _state.postValue(SearchScreenStatus.ShowHistory(searchInteractor.getTracksHistory()))
    }

    //Debounce
    fun searchDebounce(toSearch: String) {
        searchDebounceJob?.cancel()
        searchDebounceJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY_MILLIS)
            searchForTracks(toSearch)
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

    //Search
    fun searchForTracks(toSearch: String) {
        searchDebounceJob?.cancel()
        setState(SearchScreenStatus.Loading)
        viewModelScope.launch {
            searchInteractor
                .searchForTracks(toSearch)
                .collect { pair ->
                    getTracksResult(pair.first, pair.second)
                }
        }
    }

    private fun getTracksResult(foundTracks: List<Track>?, errorMessage: Boolean?) {
        val trackList = mutableListOf<Track>()
        if (foundTracks != null) {
            trackList.addAll(foundTracks)
            setState(SearchScreenStatus.ShowTracks(trackList))
        }

        when {
            errorMessage != null -> {
                setState(SearchScreenStatus.ConnectionError)
            }

            trackList.isEmpty() -> {
                setState(SearchScreenStatus.NotFound)
            }
        }
    }

    //History
    fun addToSearchHistory(track: Track) {
        val tempList = searchInteractor.getTracksHistory()
        if (tempList.contains(track)) {
            tempList.remove(track)
        }
        if (tempList.size > 9) {
            tempList.removeFirst()
            tempList.add(track)
            searchInteractor.addHistoryToLocalDb(tempList)
            searchHistory.addAll(tempList)
        } else {
            tempList.add(track)
            searchInteractor.addHistoryToLocalDb(tempList)
            searchHistory.addAll(tempList)
        }

    }

    fun getSearchHistory(): List<Track> {
        if (searchInteractor.getTracksHistory().isNotEmpty()) {
            return searchInteractor.getTracksHistory()
        }
        return emptyList()
    }

    fun clearSearchHistory() {
        searchInteractor.clearHistory()
        searchHistory.clear()
        showHistory()
    }

    //State
    private fun setState(status: SearchScreenStatus) {
        _state.postValue(status)
    }

    companion object {
        //For Delay
        const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
        const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L
    }
}