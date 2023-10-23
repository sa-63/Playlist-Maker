package com.project.playlistmaker.di

import com.project.playlistmaker.favourite.ui.viewmodel.FavTracksViewModel
import com.project.playlistmaker.playerscreen.ui.viewmodel.PlayerViewModel
import com.project.playlistmaker.playlist.ui.viewmodels.NewPlaylistViewModel
import com.project.playlistmaker.playlist.ui.viewmodels.PlaylistTracksViewModel
import com.project.playlistmaker.playlist.ui.viewmodels.PlaylistsViewModel
import com.project.playlistmaker.searchscreen.ui.view_model.SearchViewModel
import com.project.playlistmaker.settingsscreen.ui.viewmodel.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

class ViewModelModule {
    val viewModelModule = module {

        //Search
        viewModel {
            SearchViewModel(get())
        }

        //Player
        viewModel {
            PlayerViewModel(get(), get(), get(), get())
        }

        //Settings
        viewModel {
            SettingsViewModel(get(), get())
        }

        //FavTracks
        viewModel {
            FavTracksViewModel(get())
        }

        //Playlist
        viewModel {
            PlaylistsViewModel(get())
        }

        //NewPlaylist
        viewModel {
            NewPlaylistViewModel(get())
        }

        //PlaylistTracks
        viewModel {
            PlaylistTracksViewModel(get(), get())
        }
    }
}