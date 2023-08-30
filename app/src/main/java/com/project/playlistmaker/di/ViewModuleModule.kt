package com.project.playlistmaker.di

import com.project.playlistmaker.mediascreen.ui.viewmodels.FavTracksViewModel
import com.project.playlistmaker.mediascreen.ui.viewmodels.MyPlaylistsViewModel
import com.project.playlistmaker.mediascreen.ui.viewmodels.MediaLibraryViewModel
import com.project.playlistmaker.playerscreen.ui.viewmodel.PlayerViewModel
import com.project.playlistmaker.searchscreen.ui.view_model.SearchViewModel
import com.project.playlistmaker.settingsscreen.ui.viewmodel.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

class ViewModuleModule {

    val viewModelModule = module {

        //Search
        viewModel {
            SearchViewModel(get())
        }

        //Player
        viewModel {
            PlayerViewModel(get())
        }

        //Settings
        viewModel {
            SettingsViewModel(get(), get())
        }

        //MediaLibrary
        viewModel {
            MediaLibraryViewModel()
        }

        //Fragments
        viewModel {
            FavTracksViewModel()
        }

        viewModel {
            MyPlaylistsViewModel()
        }
    }
}