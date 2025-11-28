package com.example.mobilappcaseemarket.data

import com.example.mobilappcaseemarket.data.local.FavouriteDao
import com.example.mobilappcaseemarket.data.model.FavouriteItem

class FakeFavouriteDao : FavouriteDao {

    private val favourites = mutableListOf<FavouriteItem>()

    override suspend fun addToFavorites(item: FavouriteItem) {
        if (!favourites.contains(item)) {
            favourites.add(item)
        }
    }

    override suspend fun removeFromFavorites(item: FavouriteItem) {
        favourites.remove(item)
    }

    override suspend fun getAllFavorites(): List<FavouriteItem> {
        return favourites
    }

    override suspend fun isFavorite(productId: String): Boolean {
        return favourites.any { it.productId == productId }
    }
}
