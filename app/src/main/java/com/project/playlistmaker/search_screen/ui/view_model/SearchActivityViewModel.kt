package com.project.playlistmaker.search_screen.ui.view_model

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.playlistmaker.creator.Creator
import com.project.playlistmaker.search_screen.domain.models.NetworkError
import com.project.playlistmaker.search_screen.domain.models.Track
import com.project.playlistmaker.search_screen.domain.search_interactor.SearchInteractor
import com.project.playlistmaker.search_screen.ui.models.SearchStatusResult

class SearchActivityViewModel(private val searchInteractor: SearchInteractor) : ViewModel() {
    //For Debounce
    private val mainHandler = Handler(Looper.getMainLooper())
    private var isClickAllowed = true
    private lateinit var searchRunnable: Runnable

    //LiveData
    //Request status
    private val searchStatusResultStateLiveData = MutableLiveData<SearchStatusResult>()
    fun observeSearchStatusResultLiveData(): LiveData<SearchStatusResult> =
        searchStatusResultStateLiveData

    //Tracks
    private val tracksResults = MutableLiveData<List<Track>>()
    fun observeTracksResultLiveData(): LiveData<List<Track>> = tracksResults

    //History
    private val searchHistoryLiveData = MutableLiveData<ArrayList<Track>>()
    fun observeSearchHistory(): LiveData<ArrayList<Track>> = searchHistoryLiveData

    init {
        tracksResults.postValue(emptyList())
    }

    companion object {
        //ViewModelProvider
        fun getViewModelFactory(context: Context): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SearchActivityViewModel(
                        Creator.provideSearchInteractor(context)
                    ) as T
                }
            }

        //For Delay
        const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
        const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L
    }

    //Debounce
    fun searchDebounce(toSearch: String) {
        searchRunnable = Runnable {
            searchForTracks(toSearch)
        }
        mainHandler.removeCallbacks(searchRunnable)
        mainHandler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY_MILLIS)
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
        searchInteractor.searchForTracks(
            toSearch,
            onSuccess = { trackList ->
                tracksResults.postValue(trackList)
            },
            onError = { networkError ->
                if (networkError == NetworkError.CONNECTION_ERROR) {
                    searchStatusResultStateLiveData.postValue(SearchStatusResult.CONNECTION_ERROR)
                } else {
                    searchStatusResultStateLiveData.postValue(SearchStatusResult.NOTING_FOUND_ERROR)
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
            searchHistoryLiveData.postValue(tempList)
        } else {
            tempList.add(track)
            searchInteractor.addHistoryToLocalDb(tempList)
            searchHistoryLiveData.postValue(tempList)
        }

    }

    fun addHistoryToLocalDb() {
        searchInteractor.addHistoryToLocalDb(searchHistoryLiveData.value!!)
    }

    fun getTracksHistory() {
        searchHistoryLiveData.postValue(searchInteractor.getTracksHistory())
    }

    fun clearSearchHistory() {
        searchInteractor.clearHistory()
        searchHistoryLiveData.postValue(arrayListOf())
    }
}