package com.project.playlistmaker.searchscreen.data.dto

import com.project.playlistmaker.searchscreen.domain.models.Track

class TracksResponse(val results: ArrayList<Track>) : ResponseResultCode()
