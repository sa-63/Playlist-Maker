package com.project.playlistmaker.settings_screen.data.storage

import com.project.playlistmaker.settings_screen.domain.model.ThemeSettings

interface SettingsThemeStorage {
    fun saveThemeSettings(settings: ThemeSettings)
    fun getThemeSettings(): ThemeSettings
}