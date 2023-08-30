package com.project.playlistmaker.searchscreen.data.localstorage

import com.project.playlistmaker.searchscreen.domain.models.Track

interface SearchHistoryStorage {

    fun addHistoryToLocalDb(historyList: ArrayList<Track>)

    fun getHistoryFromLocalDb(): ArrayList<Track>

    fun clearHistoryForLocalDb()
}