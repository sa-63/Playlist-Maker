package com.project.playlistmaker.data.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesSearchApi {

    @GET("/search?entity=song")
    fun search(@Query("term") text: String): Call<SongsResponse>
}