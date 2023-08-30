package com.project.playlistmaker.searchscreen.data.network.searchapi

import com.project.playlistmaker.searchscreen.data.network.searchapiresponse.TracksResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesSearchApi {
    @GET("/search?entity=song")
    fun search(@Query("term") text: String): Call<TracksResponse>
}