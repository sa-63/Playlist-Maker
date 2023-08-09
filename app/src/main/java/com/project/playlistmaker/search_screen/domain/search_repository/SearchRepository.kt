package com.project.playlistmaker.search_screen.domain.search_repository

import com.project.playlistmaker.search_screen.domain.models.NetworkError
import com.project.playlistmaker.search_screen.domain.models.Track

interface SearchRepository {

    fun addHistoryToLocalDb(tracksList: ArrayList<Track>)

    fun searchForTracks(query: String, onSuccess: (ArrayList<Track>) -> Unit, onError: (NetworkError) -> Unit)

    fun getTracksHistory(): ArrayList<Track>

    fun clearHistory()
}