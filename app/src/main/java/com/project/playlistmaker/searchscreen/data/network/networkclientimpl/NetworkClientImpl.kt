package com.project.playlistmaker.searchscreen.data.network.networkclientimpl

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.project.playlistmaker.searchscreen.data.dto.ResponseResultCode
import com.project.playlistmaker.searchscreen.data.dto.TracksRequest
import com.project.playlistmaker.searchscreen.data.network.networkclient.NetworkClient
import com.project.playlistmaker.searchscreen.data.network.searchapi.ItunesSearchApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NetworkClientImpl(
    private val context: Context,
    private val itunesSearchApi: ItunesSearchApi
) : NetworkClient {

    override suspend fun doRequest(dto: Any): ResponseResultCode {
        if (!isConnected()) {
            return ResponseResultCode().apply { resultCode = -1 }
        }
        if (dto !is TracksRequest) {
            return ResponseResultCode().apply { resultCode = 400 }
        }

        return withContext(Dispatchers.IO) {
            try {
                val response = itunesSearchApi.search(dto.expression)
                response.apply { resultCode = 200 }
            } catch (e: Throwable) {
                ResponseResultCode().apply { resultCode = 500 }
            }
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}