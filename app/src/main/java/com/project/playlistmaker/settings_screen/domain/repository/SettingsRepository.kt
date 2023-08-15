package com.project.playlistmaker.settings_screen.domain.repository

import com.project.playlistmaker.settings_screen.domain.model.ThemeSettings

interface SettingsRepository {
    fun getThemeSettings(): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings)
}