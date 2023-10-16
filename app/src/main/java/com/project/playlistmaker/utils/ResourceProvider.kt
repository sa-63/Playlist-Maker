package com.project.playlistmaker.utils

interface ResourceProvider {
    fun getString(resId: Int): String
}