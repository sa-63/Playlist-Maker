package com.project.playlistmaker.playlist.domain.models.states

import com.project.playlistmaker.createplaylist.domain.model.MyPlaylist
import com.project.playlistmaker.searchscreen.domain.models.Track

sealed class StateTracksInPlaylist {

    class NoTracks : StateTracksInPlaylist()

    data class WithTracks(val listTracks: List<Track>, val durationSumTime: Long) :
        StateTracksInPlaylist()

    data class DeletedTrack(
        val listTracks: List<Track>,
        val durationSumTime: Long,
        val counterTracks: Int
    ) : StateTracksInPlaylist()

    class ErrorStateTracks : StateTracksInPlaylist()

    class DeletedPlaylist: StateTracksInPlaylist()

    class InitPlaylist(val myPlaylist: MyPlaylist): StateTracksInPlaylist()
}
