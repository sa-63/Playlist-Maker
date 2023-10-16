package com.project.playlistmaker.playerscreen.ui.model

sealed interface ToastState {
    object None : ToastState
    data class Show(val additionalMessage: String) : ToastState
}