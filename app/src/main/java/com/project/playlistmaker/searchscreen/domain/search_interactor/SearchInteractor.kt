package com.project.playlistmaker.searchscreen.domain.search_interactor

import com.project.playlistmaker.searchscreen.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface SearchInteractor {
    fun searchForTracks(expression: String): Flow<Pair<List<Track>?, Boolean?>>

    fun addHistoryToLocalDb(historyList: ArrayList<Track>)

    fun getTracksHistory(): ArrayList<Track>

    fun clearHistory()
}