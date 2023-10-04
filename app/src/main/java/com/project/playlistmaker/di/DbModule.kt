package com.project.playlistmaker.di

import androidx.room.Room
import com.project.playlistmaker.mediascreen.favourite.data.mapper.TrackDbMapper
import com.project.playlistmaker.mediascreen.favourite.data.database.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

class DbModule {
    val dbModule = module {
        single {
            Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
                .build()
        }

        factory {
            TrackDbMapper()
        }
    }
}