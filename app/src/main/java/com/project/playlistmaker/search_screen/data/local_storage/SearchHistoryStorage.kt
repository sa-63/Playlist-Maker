package com.project.playlistmaker.search_screen.data.local_storage

import com.project.playlistmaker.search_screen.domain.models.Track

interface SearchHistoryStorage {

    fun addHistoryToLocalDb(historyList: ArrayList<Track>)

    fun getHistoryFromLocalDb(): ArrayList<Track>

    fun clearHistoryForLocalDb()
}