package com.project.playlistmaker.presentation.ui.activity_player.play_back

import android.widget.ImageButton
import com.project.playlistmaker.R
import com.project.playlistmaker.domain.player.PlayerState
import com.project.playlistmaker.domain.player.player_interactor.PlayerInterector
import com.project.playlistmaker.presentation.ui.activity_player.updateDurationView.UpdateDurationTask

class PlayBackControl(
    private val playerInterectorImpl: PlayerInterector,
    private val updateDurationTask: UpdateDurationTask,
    private val textViewToUpdate: ImageButton,
    private val url: String
) {
    fun playbackControl() {
        when (playerInterectorImpl.getPlayerState()) {
            PlayerState.STATE_PLAYING -> {
                pausePlayer()
            }

            PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED -> {
                startPlayer()
            }

            PlayerState.STATE_DEFAULT -> {
                playerInterectorImpl.preparePlayer(url)
                playerInterectorImpl.startPlayer()
            }
        }
        playerInterectorImpl.setOnCompletionListener {
            textViewToUpdate.setImageResource(R.drawable.ic_play)
        }
    }

    private fun startPlayer() {
        textViewToUpdate.setImageResource(R.drawable.pause_btn)
        playerInterectorImpl.startPlayer()
        updateDurationTask.startDurationTask()
    }

    private fun pausePlayer() {
        updateDurationTask.removeCallbacks()
        textViewToUpdate.setImageResource(R.drawable.ic_play)
        playerInterectorImpl.pausePlayer()
    }
}