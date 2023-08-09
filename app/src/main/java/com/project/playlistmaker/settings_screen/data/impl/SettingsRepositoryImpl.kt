package com.project.playlistmaker.settings_screen.data.impl

import com.project.playlistmaker.settings_screen.domain.repository.SettingsRepository
import com.project.playlistmaker.settings_screen.data.storage.SettingsThemeStorage
import com.project.playlistmaker.settings_screen.domain.model.ThemeSettings

class SettingsRepositoryImpl(private val storage: SettingsThemeStorage): SettingsRepository {

    override fun getThemeSettings(): ThemeSettings {
        return storage.getThemeSettings()
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        storage.saveThemeSettings(settings)
    }

}