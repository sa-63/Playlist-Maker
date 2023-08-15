package com.project.playlistmaker.settings_screen.data.storage

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.project.playlistmaker.settings_screen.domain.model.ThemeSettings

class SharedPrefsThemeStorage(private val context: Context) : SettingsThemeStorage {
    companion object {
        const val SHARED_PREF_SETTINGS = "settings_preferences"
        const val DARK_THEME = "dark_theme"
    }

    private val sharedPrefs: SharedPreferences =
        context.getSharedPreferences(SHARED_PREF_SETTINGS, AppCompatActivity.MODE_PRIVATE)

    override fun saveThemeSettings(settings: ThemeSettings) {
        sharedPrefs.edit()
            .putBoolean(DARK_THEME, settings.darkTheme)
            .apply()
    }

    override fun getThemeSettings(): ThemeSettings {
        val darkTheme = sharedPrefs.getBoolean(DARK_THEME, false)
        return ThemeSettings(darkTheme)
    }
}