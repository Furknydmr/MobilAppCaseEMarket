package com.example.mobilappcaseemarket.data

import com.example.mobilappcaseemarket.data.model.FavouriteItem
import com.example.mobilappcaseemarket.data.repository.FavouriteRepositoryInterface

class FakeFavouriteRepository : FavouriteRepositoryInterface {

    private val favs = mutableSetOf<String>()

    override suspend fun addToFavourite(productId: String) {
        favs.add(productId)
    }

    override suspend fun removeFromFavourite(productId: String) {
        favs.remove(productId)
    }

    override suspend fun getAllFavourites(): List<FavouriteItem> {
        return favs.map { FavouriteItem(it) }
    }

    override suspend fun isFavourite(productId: String): Boolean {
        return favs.contains(productId)
    }
}
