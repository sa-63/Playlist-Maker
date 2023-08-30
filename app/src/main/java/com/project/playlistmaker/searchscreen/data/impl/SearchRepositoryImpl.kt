package com.project.playlistmaker.searchscreen.data.impl

import com.project.playlistmaker.searchscreen.data.localstorage.SearchHistoryStorage
import com.project.playlistmaker.searchscreen.data.network.networkclient.NetworkClient
import com.project.playlistmaker.searchscreen.domain.models.NetworkError
import com.project.playlistmaker.searchscreen.domain.models.Track
import com.project.playlistmaker.searchscreen.domain.search_repository.SearchRepository

class SearchRepositoryImpl(private val networkClient: NetworkClient,
                           private val searchHistoryStorage: SearchHistoryStorage) :
    SearchRepository {
    override fun addHistoryToLocalDb(tracksList: ArrayList<Track>) {
        searchHistoryStorage.addHistoryToLocalDb(tracksList)
    }

    override fun searchForTracks(query: String, onSuccess: (ArrayList<Track>) -> Unit, onError: (NetworkError) -> Unit)  {
        networkClient.doRequest(query, onSuccess, onError)
    }

    override fun getTracksHistory(): ArrayList<Track> {
        return searchHistoryStorage.getHistoryFromLocalDb()
    }

    override fun clearHistory() {
        searchHistoryStorage.clearHistoryForLocalDb()
    }
}