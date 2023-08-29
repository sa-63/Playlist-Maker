package com.project.playlistmaker.settingsscreen.domain.impl

import com.project.playlistmaker.settingsscreen.domain.interactor.SettingsInteractor
import com.project.playlistmaker.settingsscreen.domain.model.ThemeSettings
import com.project.playlistmaker.settingsscreen.domain.repository.SettingsRepository

class SettingsInteractorImpl(private val repository: SettingsRepository): SettingsInteractor {
    override fun getThemeSettings(): ThemeSettings {
        return repository.getThemeSettings()
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        repository.updateThemeSetting(settings)
    }
}