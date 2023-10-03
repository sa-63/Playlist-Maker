package com.project.playlistmaker.mediascreen.ui.models

import com.project.playlistmaker.searchscreen.domain.models.Track

sealed interface FavTracksState {
    data class Content(
        val tracks: List<Track>
    ) : FavTracksState

    object Empty : FavTracksState
}