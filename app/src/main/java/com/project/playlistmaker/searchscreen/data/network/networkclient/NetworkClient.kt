package com.project.playlistmaker.searchscreen.data.network.networkclient

import com.project.playlistmaker.searchscreen.domain.models.NetworkError
import com.project.playlistmaker.searchscreen.domain.models.Track

interface NetworkClient {
    fun doRequest(query: String, onSuccess: (ArrayList<Track>) -> Unit, onError: (NetworkError) -> Unit)
}