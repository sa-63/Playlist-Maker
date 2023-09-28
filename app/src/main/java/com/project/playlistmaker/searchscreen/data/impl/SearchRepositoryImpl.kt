package com.project.playlistmaker.searchscreen.data.impl

import com.project.playlistmaker.searchscreen.data.dto.TracksRequest
import com.project.playlistmaker.searchscreen.data.dto.TracksResponse
import com.project.playlistmaker.searchscreen.data.localstorage.SearchHistoryStorage
import com.project.playlistmaker.searchscreen.data.network.networkclient.NetworkClient
import com.project.playlistmaker.searchscreen.domain.models.Track
import com.project.playlistmaker.searchscreen.domain.search_repository.SearchRepository
import com.project.playlistmaker.utils.ResponseCheck
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchRepositoryImpl(
    private val networkClient: NetworkClient,
    private val searchHistoryStorage: SearchHistoryStorage
) :
    SearchRepository {
    override fun addHistoryToLocalDb(tracksList: ArrayList<Track>) {
        searchHistoryStorage.addHistoryToLocalDb(tracksList)
    }

    override fun searchForTracks(expression: String): Flow<ResponseCheck<List<Track>>> = flow {
        val response = networkClient.doRequest(TracksRequest(expression))

        when (response.resultCode) {

            -1 -> {
                emit(ResponseCheck.Error(isFailed = false))
            }

            200 -> {
                emit(ResponseCheck.Success((response as TracksResponse).results.map {
                    Track(
                        trackName = it.trackName,
                        artistName = it.artistName,
                        trackTimeMillis = it.trackTimeMillis,
                        artworkUrl100 = it.artworkUrl100,
                        trackId = it.trackId,
                        collectionName = it.collectionName,
                        releaseDate = it.releaseDate,
                        primaryGenreName = it.primaryGenreName,
                        country = it.country,
                        previewUrl = it.previewUrl
                    )
                }))
            }

            else -> {
                emit(ResponseCheck.Error(isFailed = true))
            }
        }
    }

    override fun getTracksHistory(): ArrayList<Track> {
        return searchHistoryStorage.getHistoryFromLocalDb()
    }

    override fun clearHistory() {
        searchHistoryStorage.clearHistoryForLocalDb()
    }
}