package com.project.playlistmaker.playerscreen.domain.impl

import com.project.playlistmaker.playerscreen.domain.playerinteractor.PlayerInteractor
import com.project.playlistmaker.playerscreen.domain.playerrepository.PlayerRepository

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