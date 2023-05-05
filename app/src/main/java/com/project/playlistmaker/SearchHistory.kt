package com.project.playlistmaker

class SearchHistory {
    private var tracksSearchHistory: MutableMap<Int, TrackDto> = mutableMapOf()

    fun addTrackToHistory(trackDto: TrackDto) {
        //Проверки
        tracksSearchHistory[trackDto.trackId] = trackDto
    }

    fun getTrackHistory(): ArrayList<TrackDto> {
        return ArrayList(tracksSearchHistory.values)
    }
}