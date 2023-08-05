package com.project.playlistmaker.player_screen.ui.player_utils.update_duration_view

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import com.project.playlistmaker.R
import com.project.playlistmaker.player_screen.ui.view_model.ActivityPlayerViewModel
import com.project.playlistmaker.use_cases.utilities.DataFormat

class UpdateDurationTaskImpl(
    private val activityPlayerViewModel: ActivityPlayerViewModel,
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
                        activityPlayerViewModel.getDuration(),
                        activityPlayerViewModel.getCurrentPosition()
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