package com.project.playlistmaker.di

import com.project.playlistmaker.player_screen.ui.view_model.ActivityPlayerViewModel
import com.project.playlistmaker.search_screen.ui.view_model.SearchActivityViewModel
import com.project.playlistmaker.settings_screen.ui.view_model.ActivitySettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

class ViewModuleModule {

    val viewModelModule = module {

        //Search
        viewModel {
            SearchActivityViewModel(get())
        }

        //Player
        viewModel {
            ActivityPlayerViewModel(get())
        }

        //Settings
        viewModel {
            ActivitySettingsViewModel(get(), get())
        }
    }
}