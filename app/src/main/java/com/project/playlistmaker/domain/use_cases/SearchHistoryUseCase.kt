package com.project.playlistmaker.domain.use_cases

import com.project.playlistmaker.domain.models.Track

class SearchHistoryUseCase(private val tracksSearchHistory: ArrayList<Track>) {

    fun addTrackToHistory(track: Track) {
        if (tracksSearchHistory.contains(track)) {
            tracksSearchHistory.remove(track)
        }
        if (tracksSearchHistory.size > 9) {
            tracksSearchHistory.removeFirst()
            tracksSearchHistory.add(track)
        } else {
            tracksSearchHistory.add(track)
        }
    }
}