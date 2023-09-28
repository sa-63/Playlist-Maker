package com.project.playlistmaker.searchscreen.domain.impl

import com.project.playlistmaker.searchscreen.domain.models.Track
import com.project.playlistmaker.searchscreen.domain.search_interactor.SearchInteractor
import com.project.playlistmaker.searchscreen.domain.search_repository.SearchRepository
import com.project.playlistmaker.utils.ResponseCheck
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchInteractorImpl(private val searchRepository: SearchRepository) : SearchInteractor {

    override fun addHistoryToLocalDb(historyList: ArrayList<Track>) {
        searchRepository.addHistoryToLocalDb(historyList)
    }

    override fun searchForTracks(expression: String): Flow<Pair<List<Track>?, Boolean?>> {
        return searchRepository.searchForTracks(expression).map { result ->
            when (result) {
                is ResponseCheck.Success -> {
                    Pair(result.data, null)
                }

                is ResponseCheck.Error -> {
                    Pair(null, result.isFailed)
                }
            }
        }
    }

    override fun getTracksHistory(): ArrayList<Track> {
        return searchRepository.getTracksHistory()
    }

    override fun clearHistory() {
        searchRepository.clearHistory()
    }
}