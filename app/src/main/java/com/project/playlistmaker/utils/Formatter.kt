package com.project.playlistmaker.utils

import android.content.Context
import com.project.playlistmaker.R
import java.text.SimpleDateFormat
import java.util.Locale

object Formatter {

    const val MILLES_IN_MIN = 60000

    fun formattingTheEndTracks(countTracks: Int = 0, context: Context): String {

        when (countTracks % 100) {
            11, 12, 13, 14 -> return "$countTracks ${context.getString(R.string.option_one_for_many_tracks)}"
        }

        return when(countTracks % 10){
            1 -> "$countTracks ${context.getString(R.string.option_one_for_one_track)}"
            2,3,4 ->  "$countTracks ${context.getString(R.string.option_two_for_one_track)}"
            else -> "$countTracks ${context.getString(R.string.option_one_for_many_tracks)}"
        }
    }

    fun formattingTheEndMinutes(durationSumInMillis:Long?, context: Context): String {

        val minutes:Int

        if(durationSumInMillis == null){
            minutes = 0
        }else{
            minutes = (durationSumInMillis/MILLES_IN_MIN).toInt()
        }

        when(minutes % 100){
            11,12,13,14 -> return "$minutes ${context.getString(R.string.option_three_for_minutes)}"
        }

        return when(minutes % 10){
            1 -> "$minutes ${context.getString(R.string.option_one_for_minutes)}"
            2,3,4 ->  "$minutes ${context.getString(R.string.option_two_for_minutes)}"
            else -> "$minutes ${context.getString(R.string.option_three_for_minutes)}"
        }
    }

    fun dateFormatting(timeInMilles:Number?): String{
        return SimpleDateFormat("mm:ss", Locale.getDefault())
            .format(timeInMilles) ?:""
    }
}