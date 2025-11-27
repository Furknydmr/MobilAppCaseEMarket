package com.example.mobilappcaseemarket.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mobilappcaseemarket.data.model.FavouriteItem

@Dao
interface FavouriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavorites(item: FavouriteItem)

    @Delete
    suspend fun removeFromFavorites(item: FavouriteItem)

    @Query("SELECT * FROM favorites")
    suspend fun getAllFavorites(): List<FavouriteItem>

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE productId = :id)")
    suspend fun isFavorite(id: String): Boolean
}