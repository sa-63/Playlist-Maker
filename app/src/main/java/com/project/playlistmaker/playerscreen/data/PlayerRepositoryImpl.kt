package com.project.playlistmaker.playerscreen.data

import android.media.MediaPlayer
import com.project.playlistmaker.playerscreen.domain.playerrepository.PlayerRepository

class PlayerRepositoryImpl(private var mediaPlayer: MediaPlayer) : PlayerRepository {

    private var isPlayerReleased = false

    override fun preparePlayer(url: String?) {
        if (isPlayerReleased) {
            mediaPlayer = MediaPlayer()
        }
        mediaPlayer.reset()
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        isPlayerReleased = false
    }

    override fun startPlayer() {
        mediaPlayer.start()
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
    }

    override fun releasePlayer() {
        if (!isPlayerReleased) {
            mediaPlayer.reset()
            mediaPlayer.release()
            isPlayerReleased = true
        }
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

    override fun startAfterPrepare(afterPrepared: () -> Unit) {
        mediaPlayer.setOnPreparedListener {
            afterPrepared()
        }
    }
}