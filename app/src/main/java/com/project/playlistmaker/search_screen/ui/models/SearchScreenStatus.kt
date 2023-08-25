package com.project.playlistmaker.search_screen.ui.models

import com.project.playlistmaker.search_screen.domain.models.Track

sealed class SearchScreenStatus {

    data class ShowTracks(val trackList: List<Track>) : SearchScreenStatus()

    data class ShowHistory(val historyList: List<Track>) : SearchScreenStatus()

    object NotFound : SearchScreenStatus()

    object ConnectionError : SearchScreenStatus()

    object Loading : SearchScreenStatus()
}