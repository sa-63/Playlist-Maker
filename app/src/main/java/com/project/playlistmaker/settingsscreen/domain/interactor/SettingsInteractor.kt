package com.project.playlistmaker.settingsscreen.domain.interactor

import com.project.playlistmaker.settingsscreen.domain.model.ThemeSettings

interface SettingsInteractor {
    fun getThemeSettings(): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings)
}