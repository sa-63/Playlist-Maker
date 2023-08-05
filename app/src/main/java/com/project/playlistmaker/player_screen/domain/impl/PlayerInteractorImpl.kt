package com.project.playlistmaker.player_screen.domain.impl

import com.project.playlistmaker.player_screen.domain.player_interactor.PlayerInteractor
import com.project.playlistmaker.player_screen.domain.player_repository.PlayerRepository

class PlayerInteractorImpl(private val playerRepository: PlayerRepository) : PlayerInteractor {
    override fun preparePlayer(url: String) {
        playerRepository.preparePlayer(url)
    }

    override fun startPlayer() {
        playerRepository.startPlayer()
    }

    override fun pausePlayer() {
        playerRepository.pausePlayer()
    }

    override fun releasePlayer() {
        playerRepository.releasePlayer()
    }

    override fun getDuration(): Int {
        return playerRepository.getDuration()
    }

    override fun getCurrentPosition(): Int {
        return playerRepository.getCurrentPosition()
    }

    override fun setOnCompletionListener(whenComplete: () -> Unit) {
        playerRepository.setOnCompletionListener(whenComplete)
    }

    override fun startAfterPrepare(afterPrepared: () -> Unit) {
        playerRepository.startAfterPrepare(afterPrepared)
    }

}