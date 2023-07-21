package com.project.playlistmaker.domain.player.player_interactor

import com.project.playlistmaker.domain.player.PlayerState

interface PlayerInteractor {
    fun preparePlayer(url: String)

    fun startPlayer()

    fun pausePlayer()

    fun releasePlayer()

    fun getPlayerState(): PlayerState

    fun getDuration(): Int

    fun getCurrentPosition(): Int

    fun setOnCompletionListener(whenComplete: () -> Unit)

    fun startAfterPrepare(afterPrepared: () -> Unit)
}