package com.project.playlistmaker.di

import com.project.playlistmaker.favourite.domain.impl.FavouriteTracksInteractorImpl
import com.project.playlistmaker.favourite.domain.interactor.FavouriteTracksInteractor
import com.project.playlistmaker.playerscreen.domain.impl.PlayerInteractorImpl
import com.project.playlistmaker.playerscreen.domain.playerinteractor.PlayerInteractor
import com.project.playlistmaker.playlist.domain.PlaylistInteractor
import com.project.playlistmaker.playlist.domain.impl.PlaylistInteractorImpl
import com.project.playlistmaker.searchscreen.domain.impl.SearchInteractorImpl
import com.project.playlistmaker.searchscreen.domain.search_interactor.SearchInteractor
import com.project.playlistmaker.settingsscreen.domain.impl.SettingsInteractorImpl
import com.project.playlistmaker.settingsscreen.domain.interactor.SettingsInteractor
import com.project.playlistmaker.sharing.domain.impl.SharingInteractorImpl
import com.project.playlistmaker.sharing.domain.interactor.SharingInteractor
import org.koin.dsl.module

class InteractorModule {
    val interactorModule = module {

        single<SearchInteractor> {
            SearchInteractorImpl(get())
        }

        single<PlayerInteractor> {
            PlayerInteractorImpl(get())
        }

        single<SettingsInteractor> {
            SettingsInteractorImpl(get())
        }

        single<SharingInteractor> {
            SharingInteractorImpl(get())
        }

        single<FavouriteTracksInteractor> {
            FavouriteTracksInteractorImpl(get())
        }

        single<PlaylistInteractor>{
            PlaylistInteractorImpl(get())
        }
    }
}