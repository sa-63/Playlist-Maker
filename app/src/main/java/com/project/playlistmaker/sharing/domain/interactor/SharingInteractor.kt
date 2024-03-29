package com.project.playlistmaker.sharing.domain.interactor

interface  SharingInteractor {
    fun shareApp()
    fun openTerms()
    fun openSupport()
    fun sharePlaylist(playlistInMessage: String)
}