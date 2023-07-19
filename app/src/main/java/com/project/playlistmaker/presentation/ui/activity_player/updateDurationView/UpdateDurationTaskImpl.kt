package com.project.playlistmaker.presentation.ui.activity_player.updateDurationView

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import com.project.playlistmaker.R
import com.project.playlistmaker.domain.use_cases.utilities.DataFormat
import com.project.playlistmaker.domain.player.player_interactor.PlayerInterector

class UpdateDurationTaskImpl(
    private val playerInterector: PlayerInterector,
    private var viewToUpdate: TextView,
    private val context: Context
) : UpdateDurationTask {

    companion object {
        private const val UPDATE_DURATION_TIME_MILLIS = 1000L
    }

    private val dataFormat = DataFormat()
    private var mainHandler: Handler = Handler(Looper.getMainLooper())

    private fun createDurationCountTask(): Runnable {
        return object : Runnable {
            override fun run() {
                val remainingTime =
                    dataFormat.convertMediaPlayerRemainingTime(
                        playerInterector.getDuration(),
                        playerInterector.getCurrentPosition()
                    )
                if (remainingTime > 0) {
                    viewToUpdate.text = dataFormat.roundTimeToMinAndSecond(remainingTime)
                    mainHandler.postDelayed(this, UPDATE_DURATION_TIME_MILLIS)
                } else {
                    viewToUpdate.text = context.getString(R.string.no_time)
                }
            }
        }
    }

    override fun removeCallbacks() {
        mainHandler.removeCallbacks(createDurationCountTask())
    }

    override fun startDurationTask() {
        mainHandler.post(
            createDurationCountTask()
        )
    }
}