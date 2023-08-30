package com.project.playlistmaker.searchscreen.domain.search_interactor

import com.project.playlistmaker.searchscreen.domain.models.NetworkError
import com.project.playlistmaker.searchscreen.domain.models.Track

interface SearchInteractor {
    fun searchForTracks(query: String, onSuccess: (ArrayList<Track>) -> Unit, onError: (NetworkError) -> Unit)

    fun addHistoryToLocalDb(historyList: ArrayList<Track>)

    fun getTracksHistory(): ArrayList<Track>

    fun clearHistory()
}