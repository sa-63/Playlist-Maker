package com.project.playlistmaker.utils

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.markodevcic.peko.PermissionRequester
import com.project.playlistmaker.di.DataModule
import com.project.playlistmaker.di.DbModule
import com.project.playlistmaker.di.InteractorModule
import com.project.playlistmaker.di.RepositoryModule
import com.project.playlistmaker.di.UtilsModule
import com.project.playlistmaker.di.ViewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        //Initialising PermissionRequester
        PermissionRequester.initialize(applicationContext)
        //Koin
        startKoin {
            androidContext(this@App)
            modules(
                DataModule().dataModule,
                RepositoryModule().repositoryModule,
                InteractorModule().interactorModule,
                ViewModelModule().viewModelModule,
                DbModule().dbModule,
                UtilsModule().utilsModule
            )
        }

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
        sharedPrefs.edit()
            .putBoolean(THEME_KEY, darkThemeEnabled)
            .apply()
    }

    companion object {
        const val SHARED_PREF_APP = "app_theme_preferences"
        const val THEME_KEY = "key_for_theme_prefs"
        lateinit var sharedPrefs: SharedPreferences
        fun newInstance() = App()
    }
}