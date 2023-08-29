package com.project.playlistmaker.settingsscreen.domain.repository

import com.project.playlistmaker.settingsscreen.domain.model.ThemeSettings

interface SettingsRepository {
    fun getThemeSettings(): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings)
}