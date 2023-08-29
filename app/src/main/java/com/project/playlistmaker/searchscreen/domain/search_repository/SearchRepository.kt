package com.project.playlistmaker.searchscreen.domain.search_repository

import com.project.playlistmaker.searchscreen.domain.models.NetworkError
import com.project.playlistmaker.searchscreen.domain.models.Track

interface SearchRepository {

    fun addHistoryToLocalDb(tracksList: ArrayList<Track>)

    fun searchForTracks(query: String, onSuccess: (ArrayList<Track>) -> Unit, onError: (NetworkError) -> Unit)

    fun getTracksHistory(): ArrayList<Track>

    fun clearHistory()
}