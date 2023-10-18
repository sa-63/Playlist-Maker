package com.project.playlistmaker.playlist.ui.model

import android.os.Parcelable
import com.project.playlistmaker.searchscreen.domain.models.Track

@kotlinx.parcelize.Parcelize
class TrackPlr(
    var trackId: Long? = null,
    var isFavorite: Boolean = false,
    var trackName: String? = null,
    var artistName: String? = null,
    var trackTimeMillis: Long? = null,
    var artworkUrl100: String? = null,
    var collectionName: String? = null,
    var releaseDate: String? = null,
    var primaryGenreName: String? = null,
    var country: String? = null,
    var previewUrl: String? = null
) : Parcelable {

    companion object {
        fun mappingTrack(track: Track): TrackPlr {
            return TrackPlr(
                trackId = track.trackId,
                isFavorite = track.isFavorite,
                trackName = track.trackName,
                artistName = track.artistName,
                trackTimeMillis = track.trackTimeMillis,
                artworkUrl100 = track.artworkUrl100,
                collectionName = track.collectionName,
                releaseDate = track.releaseDate,
                primaryGenreName = track.primaryGenreName,
                country = track.country,
                previewUrl = track.previewUrl
            )
        }

        fun mappingTrack(track:TrackPlr): Track {
            return Track(
                trackId = track.trackId,
                isFavorite = track.isFavorite,
                trackName = track.trackName,
                artistName = track.artistName,
                trackTimeMillis = track.trackTimeMillis,
                artworkUrl100 = track.artworkUrl100,
                collectionName = track.collectionName,
                releaseDate = track.releaseDate,
                primaryGenreName = track.primaryGenreName,
                country = track.country,
                previewUrl = track.previewUrl
            )
        }
    }

    fun getCoverArtwork() = artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg")
}