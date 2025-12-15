package com.example.mobilappcaseemarket.data.repository

import com.example.mobilappcaseemarket.data.model.FavouriteItem

interface FavouriteRepositoryInterface {
    suspend fun addToFavourite(productId: String)

    suspend fun removeFromFavourite(productId: String)

    suspend fun getAllFavourites(): List<FavouriteItem>

    suspend fun isFavourite(productId: String): Boolean
}
