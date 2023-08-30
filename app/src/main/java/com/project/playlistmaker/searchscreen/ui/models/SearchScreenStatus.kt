package com.project.playlistmaker.searchscreen.ui.models

import com.project.playlistmaker.searchscreen.domain.models.Track

sealed class SearchScreenStatus {

    data class ShowTracks(val trackList: List<Track>) : SearchScreenStatus()

    data class ShowHistory(val historyList: List<Track>) : SearchScreenStatus()

    object NotFound : SearchScreenStatus()

    object ConnectionError : SearchScreenStatus()

    object Loading : SearchScreenStatus()
}