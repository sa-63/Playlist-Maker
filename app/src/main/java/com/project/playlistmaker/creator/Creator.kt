package com.project.playlistmaker.creator

import com.project.playlistmaker.player_screen.data.PlayerRepositoryImpl
import com.project.playlistmaker.player_screen.domain.impl.PlayerInteractorImpl
import com.project.playlistmaker.player_screen.domain.player_interactor.PlayerInteractor
import com.project.playlistmaker.player_screen.domain.player_repository.PlayerRepository

object Creator {

    private fun getPlayerRepository(): PlayerRepository {
        return PlayerRepositoryImpl()
    }

    fun providePlayerInteractor(): PlayerInteractor {
        return PlayerInteractorImpl(getPlayerRepository())
    }
}