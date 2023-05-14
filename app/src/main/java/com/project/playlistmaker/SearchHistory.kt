package com.project.playlistmaker

class SearchHistory(private val tracksSearchHistory: ArrayList<TrackDto>) {

    fun addTrackToHistory(trackDto: TrackDto) {
        if (tracksSearchHistory.contains(trackDto)) {
            tracksSearchHistory.remove(trackDto)
        }
        if (tracksSearchHistory.size > 9) {
            tracksSearchHistory.removeFirst()
            tracksSearchHistory.add(trackDto)
        } else {
            tracksSearchHistory.add(trackDto)
        }
    }
}