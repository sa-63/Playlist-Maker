package com.project.playlistmaker.search_screen.data.network.network_client

import com.project.playlistmaker.search_screen.domain.models.NetworkError
import com.project.playlistmaker.search_screen.domain.models.Track

interface NetworkClient {
    fun doRequest(query: String, onSuccess: (ArrayList<Track>) -> Unit, onError: (NetworkError) -> Unit)
}