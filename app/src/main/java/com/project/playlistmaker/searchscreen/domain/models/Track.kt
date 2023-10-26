package com.project.playlistmaker.searchscreen.domain.models

import java.io.Serializable

data class Track (
    val trackId: Long? = null,
    val isFavorite: Boolean = false,
    val trackName: String? = null,
    val artistName: String? = null,
    val trackTimeMillis: Long? = null,
    val artworkUrl100: String? = null,
    val collectionName: String? =null,
    val releaseDate: String? = null,
    val primaryGenreName: String? = null,
    val country: String? = null,
    val previewUrl: String? = null
): Serializable {
    fun getCoverArtwork() = artworkUrl100?.replaceAfterLast('/',"512x512bb.jpg")
    fun getCover60() = artworkUrl100?.replaceAfterLast('/',"60x60bb.jpg")
}

