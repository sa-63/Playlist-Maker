package com.project.playlistmaker.di

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import com.google.gson.Gson
import com.project.playlistmaker.searchscreen.data.localstorage.SearchHistoryStorage
import com.project.playlistmaker.searchscreen.data.localstorage.SearchHistoryStorageImpl
import com.project.playlistmaker.searchscreen.data.network.networkclient.NetworkClient
import com.project.playlistmaker.searchscreen.data.network.networkclientimpl.NetworkClientImpl
import com.project.playlistmaker.searchscreen.data.network.searchapi.ItunesSearchApi
import com.project.playlistmaker.settingsscreen.data.storage.SettingsThemeStorage
import com.project.playlistmaker.settingsscreen.data.storage.SharedPrefsThemeStorage
import com.project.playlistmaker.sharing.data.impl.ExternalNavigatorImpl
import com.project.playlistmaker.sharing.domain.repository.ExternalNavigator
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DataModule {
    val dataModule = module {
        single<ItunesSearchApi> {
            val interceptor = HttpLoggingInterceptor().apply {
                this.level = HttpLoggingInterceptor.Level.BODY
            }

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()

            val itunesBaseUrl = "http://itunes.apple.com"

            Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(itunesBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ItunesSearchApi::class.java)
        }

        single<NetworkClient> {
            NetworkClientImpl(get(), get())
        }

        factory { Gson() }

        single<SharedPreferences> {
            androidContext().getSharedPreferences(
                SearchHistoryStorageImpl.SHARED_PREF_SEARCH,
                Context.MODE_PRIVATE
            )
            androidContext().getSharedPreferences(
                SharedPrefsThemeStorage.SHARED_PREF_SETTINGS,
                Context.MODE_PRIVATE
            )
        }

        single<SearchHistoryStorage> {
            SearchHistoryStorageImpl(get(), get())
        }

        single<SettingsThemeStorage> {
            SharedPrefsThemeStorage(get())
        }

        single<ExternalNavigator> {
            ExternalNavigatorImpl(androidContext())
        }

        single {
            return@single MediaPlayer()
        }
    }
}