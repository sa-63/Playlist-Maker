package com.project.playlistmaker.app

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {

    companion object {
        const val SHARED_PREF_APP = "app_theme_preferences"
        const val THEME_KEY = "key_for_theme_prefs"
    }

    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        sharedPrefs = getSharedPreferences(SHARED_PREF_APP, MODE_PRIVATE)
        if (sharedPrefs.contains(THEME_KEY)) {
            switchTheme(sharedPrefs.getBoolean(THEME_KEY, false))
        }
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        sharedPrefs.edit().putBoolean(THEME_KEY, darkThemeEnabled).apply()
    }
}