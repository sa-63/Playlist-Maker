package com.project.playlistmaker.settingsscreen.data.impl

import com.project.playlistmaker.settingsscreen.domain.repository.SettingsRepository
import com.project.playlistmaker.settingsscreen.data.storage.SettingsThemeStorage
import com.project.playlistmaker.settingsscreen.domain.model.ThemeSettings

class SettingsRepositoryImpl(private val storage: SettingsThemeStorage): SettingsRepository {

    override fun getThemeSettings(): ThemeSettings {
        return storage.getThemeSettings()
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        storage.saveThemeSettings(settings)
    }

}