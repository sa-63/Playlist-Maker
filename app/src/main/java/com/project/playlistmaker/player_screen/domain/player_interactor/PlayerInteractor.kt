package com.project.playlistmaker.player_screen.domain.player_interactor

interface PlayerInteractor {
    fun preparePlayer(url: String)

    fun startPlayer()

    fun pausePlayer()

    fun releasePlayer()

    fun getDuration(): Int

    fun getCurrentPosition(): Int

    fun setOnCompletionListener(whenComplete: () -> Unit)

    fun startAfterPrepare(afterPrepared: () -> Unit)
}