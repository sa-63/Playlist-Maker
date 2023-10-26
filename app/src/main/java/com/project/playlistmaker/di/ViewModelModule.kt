package com.project.playlistmaker.di

import com.project.playlistmaker.favourite.ui.viewmodel.FavTracksViewModel
import com.project.playlistmaker.playerscreen.ui.viewmodel.PlayerViewModel
import com.project.playlistmaker.playlist.ui.viewmodels.NewPlaylistViewModel
import com.project.playlistmaker.playlist.ui.viewmodels.PlaylistTracksViewModel
import com.project.playlistmaker.playlist.ui.viewmodels.PlaylistsViewModel
import com.project.playlistmaker.searchscreen.ui.viewmodel.SearchViewModel
import com.project.playlistmaker.settingsscreen.ui.viewmodel.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

class ViewModelModule {
    val viewModelModule = module {

        viewModel {
            SearchViewModel(get())
        }

        viewModel {
            PlayerViewModel(get(), get(), get(), get())
        }

        viewModel {
            SettingsViewModel(get(), get())
        }

        viewModel {
            FavTracksViewModel(get())
        }

        viewModel {
            PlaylistsViewModel(get())
        }

        viewModel {
            NewPlaylistViewModel(get())
        }

        viewModel {
            PlaylistTracksViewModel(get(), get())
        }
    }
}