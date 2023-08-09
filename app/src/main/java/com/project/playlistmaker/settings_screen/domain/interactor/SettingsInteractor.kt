package com.project.playlistmaker.settings_screen.domain.interactor

import com.project.playlistmaker.settings_screen.domain.model.ThemeSettings

interface SettingsInteractor {
    fun getThemeSettings(): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings)
}