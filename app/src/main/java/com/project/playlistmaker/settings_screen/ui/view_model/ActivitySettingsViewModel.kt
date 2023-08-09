package com.project.playlistmaker.settings_screen.ui.view_model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.playlistmaker.app.App
import com.project.playlistmaker.creator.Creator
import com.project.playlistmaker.settings_screen.domain.interactor.SettingsInteractor
import com.project.playlistmaker.settings_screen.domain.model.ThemeSettings
import com.project.playlistmaker.sharing.domain.interactor.SharingInteractor

class ActivitySettingsViewModel(
    private val settingsInteractor: SettingsInteractor,
    private val sharingInteractor: SharingInteractor,
    private val application: App,
) : ViewModel() {

    companion object {
        //ViewModelProvider
        fun getViewModelFactory(context: Context): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ActivitySettingsViewModel(
                        Creator.provideSettingsInteractor(context),
                        Creator.provideSharingInteractor(context),
                        App()
                    ) as T
                }
            }
    }

    private val themeSettings = MutableLiveData<ThemeSettings>()
    fun themeSettingsState(): LiveData<ThemeSettings> = themeSettings

    init {
        themeSettings.postValue(settingsInteractor.getThemeSettings())
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        val settings = ThemeSettings(darkThemeEnabled)
        themeSettings.postValue(settings)
        settingsInteractor.updateThemeSetting(settings)
        application.switchTheme(darkThemeEnabled)
    }

    fun shareApp() {
        sharingInteractor.shareApp()
    }

    fun supportEmail() {
        sharingInteractor.openSupport()
    }

    fun openAgreement() {
        sharingInteractor.openTerms()
    }
}