package com.project.playlistmaker.domain.player.player_interactor

import com.project.playlistmaker.domain.player.PlayerState

interface PlayerInterector {

    fun preparePlayer(url: String)

    fun startPlayer()

    fun pausePlayer()

    fun releasePlayer()

    fun getPlayerState(): PlayerState

    fun getDuration(): Int

    fun getCurrentPosition(): Int

    fun setOnCompletionListener(whenComplete: () -> Unit)
}