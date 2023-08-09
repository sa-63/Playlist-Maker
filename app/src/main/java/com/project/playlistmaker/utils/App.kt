package com.project.playlistmaker.utils

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {

    companion object {
        const val PREFERENCES = "preferences"
        const val SHARED_PREF_APP = "app_theme_preferences"
        const val THEME_KEY = "key_for_theme_prefs"
        lateinit var sharedMemory: SharedPreferences
    }

    private lateinit var sharedPrefs: SharedPreferences
    private var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        sharedPrefs = getSharedPreferences(SHARED_PREF_APP, MODE_PRIVATE)
        if (sharedPrefs.contains(THEME_KEY)) {
            switchTheme(sharedPrefs.getBoolean(THEME_KEY, false))
        }

        sharedMemory = getSharedPreferences(PREFERENCES, MODE_PRIVATE)
        darkTheme = sharedMemory.getBoolean(THEME_KEY, false)
        switchTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        sharedMemory.edit()
            .putBoolean(THEME_KEY, darkTheme)
            .apply()
    }
}