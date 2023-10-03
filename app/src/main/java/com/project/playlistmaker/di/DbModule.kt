package com.project.playlistmaker.di

import androidx.room.Room
import com.project.playlistmaker.data.converter.TrackDbConverter
import com.project.playlistmaker.data.database.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

class DbModule {
    val dbModule = module {
        single {
            Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
                .build()
        }

        factory {
            TrackDbConverter()
        }
    }
}