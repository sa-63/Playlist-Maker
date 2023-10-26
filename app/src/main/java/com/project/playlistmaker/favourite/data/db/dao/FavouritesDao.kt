package com.project.playlistmaker.favourite.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.project.playlistmaker.favourite.data.db.entity.FavouriteEntity

@Dao
interface FavouritesDao {
    @Insert(entity = FavouriteEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavorites(track: FavouriteEntity)

    @Query("SELECT * FROM ${FavouriteEntity.TABLE_NAME} ORDER BY favouriteAddedTimestamp DESC")
    suspend fun getTracks(): List<FavouriteEntity>

    @Query("DELETE FROM ${FavouriteEntity.TABLE_NAME} WHERE trackId = :trackId")
    suspend fun deleteFromFavorites(trackId: Long?)

    @Query("SELECT EXISTS (SELECT 1 FROM ${FavouriteEntity.TABLE_NAME}  WHERE trackId = :trackId)")
    suspend fun isFavoriteTrack(trackId: Long?): Boolean
}