package com.project.playlistmaker.di

import com.project.playlistmaker.mediascreen.data.FavouriteTracksRepositoryImpl
import com.project.playlistmaker.mediascreen.domain.repository.FavouriteTracksRepository
import com.project.playlistmaker.playerscreen.data.PlayerRepositoryImpl
import com.project.playlistmaker.playerscreen.domain.playerrepository.PlayerRepository
import com.project.playlistmaker.searchscreen.data.impl.SearchRepositoryImpl
import com.project.playlistmaker.searchscreen.domain.search_repository.SearchRepository
import com.project.playlistmaker.settingsscreen.data.impl.SettingsRepositoryImpl
import com.project.playlistmaker.settingsscreen.domain.repository.SettingsRepository
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

        //Favourites
        single<FavouriteTracksRepository> {
            FavouriteTracksRepositoryImpl(get(), get())
        }
    }
}