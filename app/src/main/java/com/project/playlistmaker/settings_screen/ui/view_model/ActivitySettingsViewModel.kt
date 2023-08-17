package com.project.playlistmaker.settings_screen.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.playlistmaker.settings_screen.domain.interactor.SettingsInteractor
import com.project.playlistmaker.settings_screen.domain.model.ThemeSettings
import com.project.playlistmaker.sharing.domain.interactor.SharingInteractor

class ActivitySettingsViewModel(
    private val settingsInteractor: SettingsInteractor,
    private val sharingInteractor: SharingInteractor
) : ViewModel() {

    private val themeSettings = MutableLiveData<ThemeSettings>()
    fun themeSettingsState(): LiveData<ThemeSettings> = themeSettings

    init {
        themeSettings.postValue(settingsInteractor.getThemeSettings())
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        val settings = ThemeSettings(darkThemeEnabled)
        themeSettings.postValue(settings)
        settingsInteractor.updateThemeSetting(settings)
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