package com.project.playlistmaker.creator

import android.content.Context
import com.project.playlistmaker.player_screen.data.PlayerRepositoryImpl
import com.project.playlistmaker.player_screen.domain.impl.PlayerInteractorImpl
import com.project.playlistmaker.player_screen.domain.player_interactor.PlayerInteractor
import com.project.playlistmaker.player_screen.domain.player_repository.PlayerRepository
import com.project.playlistmaker.search_screen.data.impl.SearchRepositoryImpl
import com.project.playlistmaker.search_screen.data.local_storage.SearchHistoryStorage
import com.project.playlistmaker.search_screen.data.local_storage.SearchHistoryStorageImpl
import com.project.playlistmaker.search_screen.data.network.network_client_impl.NetworkClientImpl
import com.project.playlistmaker.search_screen.domain.impl.SearchInteractorImpl
import com.project.playlistmaker.search_screen.domain.search_interactor.SearchInteractor
import com.project.playlistmaker.search_screen.domain.search_repository.SearchRepository
import com.project.playlistmaker.settings_screen.data.impl.SettingsRepositoryImpl
import com.project.playlistmaker.settings_screen.data.storage.SettingsThemeStorage
import com.project.playlistmaker.settings_screen.data.storage.SharedPrefsThemeStorage
import com.project.playlistmaker.settings_screen.domain.impl.SettingsInteractorImpl
import com.project.playlistmaker.settings_screen.domain.interactor.SettingsInteractor
import com.project.playlistmaker.settings_screen.domain.repository.SettingsRepository
import com.project.playlistmaker.sharing.data.impl.ExternalNavigatorImpl
import com.project.playlistmaker.sharing.domain.impl.SharingInteractorImpl
import com.project.playlistmaker.sharing.domain.interactor.SharingInteractor
import com.project.playlistmaker.sharing.domain.repository.ExternalNavigator

object Creator {

    //Player
    private fun getPlayerRepository() : PlayerRepository {
        return PlayerRepositoryImpl()
    }

    fun providePlayerInteractor() : PlayerInteractor {
        return PlayerInteractorImpl(getPlayerRepository())
    }

    //Search
    private fun provideNetworkClient(): NetworkClientImpl {
        return NetworkClientImpl()
    }

    private fun getSearchHistoryStorage(context: Context) : SearchHistoryStorage {
        return SearchHistoryStorageImpl(context)
    }

    private fun getSearchRepository(context: Context) : SearchRepository {
        return SearchRepositoryImpl(provideNetworkClient(), getSearchHistoryStorage(context))
    }

    fun provideSearchInteractor(context: Context) : SearchInteractor {
        return SearchInteractorImpl(getSearchRepository(context))
    }

    //Settings
    private fun getSettingsThemeStorage(context: Context) : SettingsThemeStorage {
        return SharedPrefsThemeStorage(context)
    }

    private fun getSettingsRepository(context: Context) : SettingsRepository {
        return SettingsRepositoryImpl(getSettingsThemeStorage(context))
    }

    fun provideSettingsInteractor(context: Context) : SettingsInteractor {
        return SettingsInteractorImpl(getSettingsRepository(context))
    }

    private fun getExternalNavigator(context: Context) : ExternalNavigator {
        return ExternalNavigatorImpl(context)
    }

    fun provideSharingInteractor(context: Context) : SharingInteractor {
        return SharingInteractorImpl(getExternalNavigator(context))
    }
}