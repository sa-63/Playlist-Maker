package com.project.playlistmaker.playlist.domain.models.states

sealed class StateAddDb {
    class NoError(val namePlaylist: String? = null) : StateAddDb()

    class Error : StateAddDb()

    class Match(val namePlaylist: String? = null) : StateAddDb()

    class NoData : StateAddDb()

}