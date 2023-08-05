package com.project.playlistmaker.player_screen.ui.player_utils.play_back

import android.widget.ImageButton
import com.project.playlistmaker.R
import com.project.playlistmaker.player_screen.domain.player_state.PlayerState
import com.project.playlistmaker.player_screen.ui.player_utils.update_duration_view.UpdateDurationTask
import com.project.playlistmaker.player_screen.ui.view_model.ActivityPlayerViewModel

class PlayBackControl(
    private val activityPlayerViewModel: ActivityPlayerViewModel,
    private val updateDurationTask: UpdateDurationTask,
    private val textViewToUpdate: ImageButton,
    private val url: String
) {

    fun playbackControl() {
        when (activityPlayerViewModel.getPlayerState()) {
            PlayerState.STATE_PLAYING -> {
                pausePlayer()
            }

            PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED -> {
                startPlayer()
            }

            PlayerState.STATE_DEFAULT -> {
                activityPlayerViewModel.preparePlayer(url)
                activityPlayerViewModel.startAfterPrepare {
                    startPlayer()
                }
            }
        }

        activityPlayerViewModel.setOnCompletionListener {
            textViewToUpdate.setImageResource(R.drawable.ic_play)
            pausePlayer()
        }
    }

    private fun startPlayer() {
        textViewToUpdate.setImageResource(R.drawable.pause_btn)
        activityPlayerViewModel.startPlayer()
        updateDurationTask.startDurationTask()
    }

    private fun pausePlayer() {
        updateDurationTask.removeCallbacks()
        textViewToUpdate.setImageResource(R.drawable.ic_play)
        activityPlayerViewModel.pausePlayer()
    }
}