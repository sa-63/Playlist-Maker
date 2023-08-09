package com.project.playlistmaker.search_screen.data.impl

import com.project.playlistmaker.search_screen.data.local_storage.SearchHistoryStorage
import com.project.playlistmaker.search_screen.data.network.network_client.NetworkClient
import com.project.playlistmaker.search_screen.domain.models.NetworkError
import com.project.playlistmaker.search_screen.domain.models.Track
import com.project.playlistmaker.search_screen.domain.search_repository.SearchRepository

class SearchRepositoryImpl(private val networkClient: NetworkClient,  private val searchHistoryStorage: SearchHistoryStorage) :
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