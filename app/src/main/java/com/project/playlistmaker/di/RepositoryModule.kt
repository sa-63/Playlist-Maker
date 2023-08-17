package com.project.playlistmaker.di

import com.project.playlistmaker.player_screen.data.PlayerRepositoryImpl
import com.project.playlistmaker.player_screen.domain.player_repository.PlayerRepository
import com.project.playlistmaker.search_screen.data.impl.SearchRepositoryImpl
import com.project.playlistmaker.search_screen.domain.search_repository.SearchRepository
import com.project.playlistmaker.settings_screen.data.impl.SettingsRepositoryImpl
import com.project.playlistmaker.settings_screen.domain.repository.SettingsRepository
import org.koin.dsl.module

class RepositoryModule {

    val repositoryModule = module {

        //Search
        single<SearchRepository> {
            SearchRepositoryImpl(get(), get())
        }

        //Player
        single<PlayerRepository> {
            PlayerRepositoryImpl(get())
        }

        //Settings
        single<SettingsRepository> {
            SettingsRepositoryImpl(get())
        }
    }
}