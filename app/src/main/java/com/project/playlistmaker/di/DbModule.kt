package com.project.playlistmaker.di

import androidx.room.Room
import com.project.playlistmaker.favourite.data.db.database.FavouritesDataBase
import com.project.playlistmaker.favourite.data.db.mappers.FavouriteDbMapper
import com.project.playlistmaker.playlist.data.db.database.PlaylistDataBase
import com.project.playlistmaker.playlist.data.storage.PlaylistStorage
import com.project.playlistmaker.playlist.data.storage.impl.PlaylistStorageImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

class DbModule {
    val dbModule = module {
        single {
            Room.databaseBuilder(androidContext(), FavouritesDataBase::class.java, DB_FAVOURITE)
                .build()
        }

        single {
            FavouriteDbMapper()
        }

        single {
            Room.databaseBuilder(androidContext(), PlaylistDataBase::class.java, DB_PLAYLISTS)
                .build()
        }

        single<PlaylistStorage> {
            PlaylistStorageImpl(get())
        }
    }

    companion object {
        private const val DB_FAVOURITE: String = "FavoriteTracks.db"
        private const val DB_PLAYLISTS: String = "Playlists.db"
    }
}