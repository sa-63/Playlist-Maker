package com.project.playlistmaker.di

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import com.google.gson.Gson
import com.project.playlistmaker.search_screen.data.local_storage.SearchHistoryStorage
import com.project.playlistmaker.search_screen.data.local_storage.SearchHistoryStorageImpl
import com.project.playlistmaker.search_screen.data.network.network_client.NetworkClient
import com.project.playlistmaker.search_screen.data.network.network_client_impl.NetworkClientImpl
import com.project.playlistmaker.search_screen.data.network.search_api.ItunesSearchApi
import com.project.playlistmaker.settings_screen.data.storage.SettingsThemeStorage
import com.project.playlistmaker.settings_screen.data.storage.SharedPrefsThemeStorage
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
        //Network
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
            NetworkClientImpl(get())
        }

        //LocalDb
        factory { Gson() }

        single<SharedPreferences> {
            //History
            androidContext().getSharedPreferences(
                SearchHistoryStorageImpl.SHARED_PREF_SEARCH,
                Context.MODE_PRIVATE
            )
            //Theme
            androidContext().getSharedPreferences(
                SharedPrefsThemeStorage.SHARED_PREF_SETTINGS,
                Context.MODE_PRIVATE
            )
        }

        single<SearchHistoryStorage> {
            SearchHistoryStorageImpl(get(), get())
        }

        //Settings
        single<ExternalNavigator> {
            ExternalNavigatorImpl(androidContext())
        }

        single<SettingsThemeStorage> {
            SharedPrefsThemeStorage(get())
        }

        //Player
        single {
            return@single MediaPlayer()
        }
    }
}