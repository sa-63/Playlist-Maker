package com.project.playlistmaker.search_screen.domain.impl

import com.project.playlistmaker.search_screen.domain.models.NetworkError
import com.project.playlistmaker.search_screen.domain.models.Track
import com.project.playlistmaker.search_screen.domain.search_interactor.SearchInteractor
import com.project.playlistmaker.search_screen.domain.search_repository.SearchRepository

class SearchInteractorImpl(private val searchRepository: SearchRepository) : SearchInteractor {

    override fun addHistoryToLocalDb(historyList: ArrayList<Track>) {
        searchRepository.addHistoryToLocalDb(historyList)
    }

    override fun searchForTracks(
        query: String,
        onSuccess: (ArrayList<Track>) -> Unit,
        onError: (NetworkError) -> Unit
    ) {
        return searchRepository.searchForTracks(query, onSuccess, onError)
    }

    override fun getTracksHistory(): ArrayList<Track> {
        return searchRepository.getTracksHistory()
    }

    override fun clearHistory() {
        searchRepository.clearHistory()
    }
}