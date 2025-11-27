package com.example.mobilappcaseemarket.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavouriteItem(
    @PrimaryKey val productId: String
)
