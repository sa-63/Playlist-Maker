package com.project.playlistmaker.creator

import com.project.playlistmaker.data.player_impl.PlayerRepositoryImpl
import com.project.playlistmaker.domain.player.impl.PlayerInteractorImpl
import com.project.playlistmaker.domain.player.player_interactor.PlayerInteractor
import com.project.playlistmaker.domain.player.player_repository.PlayerRepository

object Creator {

    private fun getPlayerRepository(): PlayerRepository {
        return PlayerRepositoryImpl()
    }

    fun providePlayerInteractor(): PlayerInteractor {
        return PlayerInteractorImpl(getPlayerRepository())
    }
}