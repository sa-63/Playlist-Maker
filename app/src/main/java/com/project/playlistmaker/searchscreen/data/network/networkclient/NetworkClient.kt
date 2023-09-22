package com.project.playlistmaker.searchscreen.data.network.networkclient

import com.project.playlistmaker.searchscreen.data.dto.ResponseResultCode

interface NetworkClient {
    suspend fun doRequest(dto: Any): ResponseResultCode
}