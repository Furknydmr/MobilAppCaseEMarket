package com.example.mobilappcaseemarket.ui.home.favourite

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilappcaseemarket.data.repository.FavouriteRepository
import kotlinx.coroutines.launch

class FavouriteViewModel(
    private val repository: FavouriteRepository
) : ViewModel() {


    val favourites = MutableLiveData<Set<String>>(emptySet())
    init {
        loadFavourites()
    }

    fun loadFavourites() {
        viewModelScope.launch {
            val favList = repository.getAllFavourites()
            favourites.value = favList.map { it.productId }.toSet()
        }
    }


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

    fun isFavourite(productId: String): Boolean {
        return favourites.value?.contains(productId) ?: false
    }
}