package com.example.mobilappcaseemarket.data.repository

import com.example.mobilappcaseemarket.data.local.FavouriteDao
import com.example.mobilappcaseemarket.data.model.FavouriteItem

class FavouriteRepository(
    private val dao: FavouriteDao
) {

    suspend fun addToFavourite(productId: String) {
        dao.addToFavorites(FavouriteItem(productId))
    }

    suspend fun removeFromFavourite(productId: String) {
        dao.removeFromFavorites(FavouriteItem(productId))
    }


    suspend fun getAllFavourites(): List<FavouriteItem> {
        return dao.getAllFavorites()
    }

    suspend fun isFavourite(productId: String): Boolean {
        return dao.isFavorite(productId)
    }
}
