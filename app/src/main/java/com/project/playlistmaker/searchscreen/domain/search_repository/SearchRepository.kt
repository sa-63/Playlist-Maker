package com.project.playlistmaker.searchscreen.domain.search_repository

import com.project.playlistmaker.searchscreen.domain.models.Track
import com.project.playlistmaker.utils.ResponseCheck
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    fun addHistoryToLocalDb(tracksList: ArrayList<Track>)

    fun searchForTracks(expression: String): Flow<ResponseCheck<List<Track>>>

    fun getTracksHistory(): ArrayList<Track>

    fun clearHistory()
}