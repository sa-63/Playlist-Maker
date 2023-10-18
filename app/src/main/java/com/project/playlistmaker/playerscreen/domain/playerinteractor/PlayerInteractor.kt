package com.project.playlistmaker.playerscreen.domain.playerinteractor

interface PlayerInteractor {
    fun preparePlayer(url: String?)

    fun startPlayer()

    fun pausePlayer()

    fun releasePlayer()

    fun getDuration(): Int

    fun getCurrentPosition(): Int

    fun setOnCompletionListener(whenComplete: () -> Unit)

    fun startAfterPrepare(afterPrepared: () -> Unit)
}