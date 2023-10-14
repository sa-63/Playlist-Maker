package com.project.playlistmaker.di

import androidx.room.Room
import com.project.playlistmaker.createplaylist.data.db.fileslocal.PrivateStorage
import com.project.playlistmaker.createplaylist.data.db.fileslocal.PrivateStorageImpl
import com.project.playlistmaker.db.mappers.PlaylistDbMapper
import com.project.playlistmaker.db.database.AppDatabase
import com.project.playlistmaker.db.mappers.TrackDbMapper
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

class DbModule {
    val dbModule = module {
        //DataBase
        single {
            Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
                .build()
        }

        //Mappers
        single {
            TrackDbMapper()
        }

        single {
            PlaylistDbMapper()
        }

        //PrivateStorage
        single<PrivateStorage> {
            PrivateStorageImpl(androidContext())
        }
    }
}