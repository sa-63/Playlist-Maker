package com.project.playlistmaker.data.player_impl

import android.media.MediaPlayer
import com.project.playlistmaker.domain.player.PlayerState
import com.project.playlistmaker.domain.player.player_interactor.PlayerInterector

class PlayerInterectorImpl : PlayerInterector {

    private var isPlayerReleased = false
    private var mediaPlayer = MediaPlayer()
    private var playerState = PlayerState.STATE_DEFAULT

    override fun preparePlayer(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = PlayerState.STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = PlayerState.STATE_PREPARED
        }
        isPlayerReleased = false
    }

    override fun startPlayer() {
        playerState = PlayerState.STATE_PLAYING
        mediaPlayer.start()
    }

    override fun pausePlayer() {
        playerState = PlayerState.STATE_PAUSED
        mediaPlayer.pause()
    }

    override fun releasePlayer() {
        mediaPlayer.release()
        isPlayerReleased = true
    }

    override fun getPlayerState(): PlayerState {
        return playerState
    }

    override fun getDuration(): Int {
        if (!isPlayerReleased) {
            return mediaPlayer.duration
        }
        return 0
    }

    override fun getCurrentPosition(): Int {
        if (!isPlayerReleased) {
            return mediaPlayer.currentPosition
        }
        return 0
    }

    override fun setOnCompletionListener(whenComplete: () -> Unit) {
        mediaPlayer.setOnCompletionListener {
            whenComplete()
        }
    }
}