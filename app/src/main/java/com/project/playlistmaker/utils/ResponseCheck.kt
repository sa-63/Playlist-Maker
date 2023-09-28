package com.project.playlistmaker.utils

sealed class ResponseCheck<T>(val data: T? = null, val isFailed: Boolean? = null) {
    class Success<T>(data: T) : ResponseCheck<T>(data)
    class Error<T>(isFailed: Boolean, data: T? = null) : ResponseCheck<T>(data, isFailed)
}