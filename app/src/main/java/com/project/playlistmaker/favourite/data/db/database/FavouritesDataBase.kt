package com.project.playlistmaker.favourite.data.db.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.project.playlistmaker.favourite.data.db.dao.FavouritesDao
import com.project.playlistmaker.favourite.data.db.entity.FavouriteEntity

@Database(
    version = 1, entities = [FavouriteEntity::class], exportSchema = true
)
abstract class FavouritesDataBase : RoomDatabase() {
    abstract fun favoritesDao(): FavouritesDao
}

