package com.project.playlistmaker.searchscreen.data.network.networkclientimpl

import com.project.playlistmaker.searchscreen.data.network.networkclient.NetworkClient
import com.project.playlistmaker.searchscreen.data.network.searchapi.ItunesSearchApi
import com.project.playlistmaker.searchscreen.data.network.searchapiresponse.TracksResponse
import com.project.playlistmaker.searchscreen.domain.models.NetworkError
import com.project.playlistmaker.searchscreen.domain.models.Track
import retrofit2.Call
import retrofit2.Callback

class NetworkClientImpl(private val itunesSearchApi: ItunesSearchApi) : NetworkClient {

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