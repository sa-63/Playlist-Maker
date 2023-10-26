package com.project.playlistmaker.utils

import android.os.Bundle

interface ArgsTransfer {
    fun postArgs(args: Bundle?)

    fun getArgs(): Bundle?
}