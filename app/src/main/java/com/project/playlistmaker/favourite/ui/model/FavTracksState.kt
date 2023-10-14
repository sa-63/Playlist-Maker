package com.project.playlistmaker.favourite.ui.model

import com.project.playlistmaker.searchscreen.domain.models.Track

sealed interface FavTracksState {
    data class Content(
        val tracks: List<Track>
    ) : FavTracksState

    object Empty : FavTracksState
}