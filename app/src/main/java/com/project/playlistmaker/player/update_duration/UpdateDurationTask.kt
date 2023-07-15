package com.project.playlistmaker.player.update_duration

interface UpdateDurationTask {
    fun removeCallbacks()

    fun startDurationTask()
}