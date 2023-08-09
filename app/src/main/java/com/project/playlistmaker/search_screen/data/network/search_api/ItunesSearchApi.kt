package com.project.playlistmaker.search_screen.data.network.search_api

import com.project.playlistmaker.search_screen.data.network.search_api_response.TracksResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesSearchApi {
    @GET("/search?entity=song")
    fun search(@Query("term") text: String): Call<TracksResponse>
}