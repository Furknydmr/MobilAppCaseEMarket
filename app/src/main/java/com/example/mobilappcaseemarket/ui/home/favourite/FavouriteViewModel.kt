package com.example.mobilappcaseemarket.ui.home.favourite

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilappcaseemarket.data.repository.FavouriteRepository
import kotlinx.coroutines.launch

class FavouriteViewModel(
    private val repository: FavouriteRepository
) : ViewModel() {

    // Favori ürün IDsini tutacağız
    val favourites = MutableLiveData<Set<String>>(emptySet())

    init {
        loadFavourites()
    }

    // Veri tabanından tüm favorileri yükler
    fun loadFavourites() {
        viewModelScope.launch {
            val favList = repository.getAllFavourites()
            favourites.value = favList.map { it.productId }.toSet()
        }
    }

    // Favori ekle / çıkar
    fun toggleFavourite(productId: String) {
        viewModelScope.launch {
            val current = favourites.value ?: emptySet()

            if (current.contains(productId)) {
                repository.removeFromFavourite(productId)
                favourites.value = current - productId
            } else {
                repository.addToFavourite(productId)
                favourites.value = current + productId
            }
        }
    }

    // UI → ürün favori mi?
    fun isFavourite(productId: String): Boolean {
        return favourites.value?.contains(productId) ?: false
    }
}