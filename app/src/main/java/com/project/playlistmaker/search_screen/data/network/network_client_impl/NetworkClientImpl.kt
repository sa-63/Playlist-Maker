package com.project.playlistmaker.search_screen.data.network.network_client_impl

import com.project.playlistmaker.search_screen.data.network.network_client.NetworkClient
import com.project.playlistmaker.search_screen.data.network.search_api.ItunesSearchApi
import com.project.playlistmaker.search_screen.data.network.search_api_response.TracksResponse
import com.project.playlistmaker.search_screen.domain.models.NetworkError
import com.project.playlistmaker.search_screen.domain.models.Track
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkClientImpl() : NetworkClient {
    private val itunesBaseUrl = "http://itunes.apple.com"

    private var interceptor = HttpLoggingInterceptor().apply {
        this.level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(itunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesSearchApi = retrofit.create(ItunesSearchApi::class.java)

    override fun doRequest(
        query: String,
        onSuccess: (ArrayList<Track>) -> Unit,
        onError: (NetworkError) -> Unit
    ) {
        itunesSearchApi.search(query).enqueue(object : Callback<TracksResponse> {
            override fun onResponse(
                call: Call<TracksResponse>,
                response: retrofit2.Response<TracksResponse>,
            ) {
                when (response.code()) {
                    200 -> {
                        if (response.body()?.results?.isNotEmpty() == true) {
                            onSuccess.invoke(response.body()?.results!!)
                        } else {
                            onError.invoke(NetworkError.NOTING_FOUND)
                        }
                    }

                    else ->
                        onError.invoke(NetworkError.CONNECTION_ERROR)
                }
            }

            override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                onError.invoke(NetworkError.CONNECTION_ERROR)
            }
        })
    }
}