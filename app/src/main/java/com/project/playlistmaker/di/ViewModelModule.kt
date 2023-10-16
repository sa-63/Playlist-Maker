package com.project.playlistmaker.di

import com.project.playlistmaker.createplaylist.ui.viewmodel.PlaylistsCreationViewModel
import com.project.playlistmaker.favourite.ui.viewmodel.FavTracksViewModel
import com.project.playlistmaker.myplaylists.viewmodel.viewmodel.MyPlayListViewModel
import com.project.playlistmaker.playerscreen.ui.viewmodel.PlayerViewModel
import com.project.playlistmaker.searchscreen.domain.models.Track
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
        viewModel { (track: Track) ->
            PlayerViewModel(track, get(), get(), get(), get())
        }

        //Settings
        viewModel {
            SettingsViewModel(get(), get())
        }

        //FavTracks
        viewModel {
            FavTracksViewModel(get())
        }

        //MyPlaylists
        viewModel {
            MyPlayListViewModel(get())
        }

        //NewPlaylists
        viewModel {
            PlaylistsCreationViewModel(get(), get(), get())
        }
    }
}