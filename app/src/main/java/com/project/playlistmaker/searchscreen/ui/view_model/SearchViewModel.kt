package com.project.playlistmaker.searchscreen.ui.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.playlistmaker.searchscreen.domain.models.NetworkError
import com.project.playlistmaker.searchscreen.domain.models.Track
import com.project.playlistmaker.searchscreen.domain.search_interactor.SearchInteractor
import com.project.playlistmaker.searchscreen.ui.models.SearchScreenStatus

class SearchViewModel(private val searchInteractor: SearchInteractor) : ViewModel() {
    //For Debounce
    private val mainHandler = Handler(Looper.getMainLooper())
    private var isClickAllowed = true
    private var searchRunnable: Runnable? = null

    //LiveData
    //Request status
    private val _state = MutableLiveData<SearchScreenStatus>()
    fun observeSearchStatusResultLiveData(): LiveData<SearchScreenStatus> = _state

    //History
    private val searchHistory = arrayListOf<Track>()

    fun notifyCleared() {
        _state.postValue(SearchScreenStatus.ShowHistory(searchInteractor.getTracksHistory()))
    }

    //Debounce
    fun searchDebounce(toSearch: String) {
        if (searchRunnable != null) {
            mainHandler.removeCallbacks(searchRunnable!!)
        }
        searchRunnable = Runnable {
            searchForTracks(toSearch)
        }
        mainHandler.postDelayed(searchRunnable!!, SEARCH_DEBOUNCE_DELAY_MILLIS)
    }

    fun clickDebounce(): Boolean {
        val currentState = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            mainHandler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY_MILLIS)
        }
        return currentState
    }

    //Search
    fun searchForTracks(toSearch: String) {
        setState(SearchScreenStatus.Loading)
        searchInteractor.searchForTracks(
            toSearch,
            onSuccess = { trackList ->
                setState(SearchScreenStatus.ShowTracks(trackList))
            },
            onError = { networkError ->
                if (networkError == NetworkError.CONNECTION_ERROR) {
                    setState(SearchScreenStatus.ConnectionError)
                } else {
                    setState(SearchScreenStatus.NotFound)
                }
            }
        )
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