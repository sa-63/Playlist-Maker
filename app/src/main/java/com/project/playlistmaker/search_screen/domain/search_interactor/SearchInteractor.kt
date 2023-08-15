package com.project.playlistmaker.search_screen.domain.search_interactor

import com.project.playlistmaker.search_screen.domain.models.NetworkError
import com.project.playlistmaker.search_screen.domain.models.Track

interface SearchInteractor {
    fun searchForTracks(query: String, onSuccess: (ArrayList<Track>) -> Unit, onError: (NetworkError) -> Unit)

    fun addHistoryToLocalDb(historyList: ArrayList<Track>)

    fun getTracksHistory(): ArrayList<Track>

    fun clearHistory()
}