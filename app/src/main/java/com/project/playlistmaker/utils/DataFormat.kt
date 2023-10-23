package com.project.playlistmaker.utils

import java.text.SimpleDateFormat
import java.util.Locale

class DataFormat {

    fun roundTimeToMinAndSecond(time: Int): String {
        return String.format(
            TIME_FORMAT, time / CONVERT_TO_TOTAL_TIME, time % CONVERT_TO_TOTAL_TIME
        )
    }

    fun convertTimeToMnSs(timeInMilles: Long?): String {
        return SimpleDateFormat(MIN_SEC_FORMAT, Locale.getDefault())
            .format(timeInMilles) ?: ""
    }

    fun convertMediaPlayerRemainingTime(duration: Int, currentPosition: Int): Int {
        return (duration - currentPosition) / CONVERT_TO_SEC
    }

    companion object {
        private const val TIME_FORMAT = "%d:%02d"
        private const val MIN_SEC_FORMAT = "mm:ss"
        private const val CONVERT_TO_TOTAL_TIME = 60
        private const val CONVERT_TO_SEC = 1000
    }
}