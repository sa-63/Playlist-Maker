package com.project.playlistmaker.settingsscreen.data.storage

import android.content.SharedPreferences
import com.project.playlistmaker.settingsscreen.domain.model.ThemeSettings

class SharedPrefsThemeStorage(private val sharedPrefs: SharedPreferences) : SettingsThemeStorage {

    override fun saveThemeSettings(settings: ThemeSettings) {
        sharedPrefs.edit()
            .putBoolean(DARK_THEME, settings.darkTheme)
            .apply()
    }

    override fun getThemeSettings(): ThemeSettings {
        val darkTheme = sharedPrefs.getBoolean(DARK_THEME, false)
        return ThemeSettings(darkTheme)
    }

    companion object {
        const val SHARED_PREF_SETTINGS = "settings_preferences"
        const val DARK_THEME = "dark_theme"
    }
}