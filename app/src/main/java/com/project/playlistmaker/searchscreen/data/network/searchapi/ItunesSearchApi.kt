package com.project.playlistmaker.searchscreen.data.network.searchapi

import com.project.playlistmaker.searchscreen.data.dto.TracksResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesSearchApi {
    @GET("/search?entity=song")
    suspend fun search(@Query("term") text: String): TracksResponse
}