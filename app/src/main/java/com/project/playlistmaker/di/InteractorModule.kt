package com.project.playlistmaker.di

import com.project.playlistmaker.player_screen.domain.impl.PlayerInteractorImpl
import com.project.playlistmaker.player_screen.domain.player_interactor.PlayerInteractor
import com.project.playlistmaker.search_screen.domain.impl.SearchInteractorImpl
import com.project.playlistmaker.search_screen.domain.search_interactor.SearchInteractor
import com.project.playlistmaker.settings_screen.domain.impl.SettingsInteractorImpl
import com.project.playlistmaker.settings_screen.domain.interactor.SettingsInteractor
import com.project.playlistmaker.sharing.domain.impl.SharingInteractorImpl
import com.project.playlistmaker.sharing.domain.interactor.SharingInteractor
import org.koin.dsl.module

class InteractorModule {

    val interactorModule = module {

        //Search
        single<SearchInteractor> {
            SearchInteractorImpl(get())
        }

        //Player
        single<PlayerInteractor> {
            PlayerInteractorImpl(get())
        }

        //Settings
        single<SettingsInteractor> {
            SettingsInteractorImpl(get())
        }

        single<SharingInteractor> {
            SharingInteractorImpl(get())
        }
    }
}