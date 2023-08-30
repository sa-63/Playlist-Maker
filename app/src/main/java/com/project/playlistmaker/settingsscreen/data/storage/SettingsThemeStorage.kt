package com.project.playlistmaker.settingsscreen.data.storage

import com.project.playlistmaker.settingsscreen.domain.model.ThemeSettings

interface SettingsThemeStorage {
    fun saveThemeSettings(settings: ThemeSettings)
    fun getThemeSettings(): ThemeSettings
}