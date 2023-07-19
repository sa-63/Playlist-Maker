package com.project.playlistmaker.creator

import com.project.playlistmaker.data.player_impl.PlayerInterectorImpl
import com.project.playlistmaker.domain.player.player_interactor.PlayerInterector

object Creator {

    fun providePlayerRepository(): PlayerInterector {
        return PlayerInterectorImpl()
    }

}