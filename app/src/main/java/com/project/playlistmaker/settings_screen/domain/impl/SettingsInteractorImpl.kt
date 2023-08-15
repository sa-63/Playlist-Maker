package com.project.playlistmaker.settings_screen.domain.impl

import com.project.playlistmaker.settings_screen.domain.interactor.SettingsInteractor
import com.project.playlistmaker.settings_screen.domain.model.ThemeSettings
import com.project.playlistmaker.settings_screen.domain.repository.SettingsRepository

class SettingsInteractorImpl(private val repository: SettingsRepository): SettingsInteractor {
    override fun getThemeSettings(): ThemeSettings {
        return repository.getThemeSettings()
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        repository.updateThemeSetting(settings)
    }
}