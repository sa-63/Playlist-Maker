package com.project.playlistmaker.player.player_interactor

import com.project.playlistmaker.player.PlayerState

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