package com.example.mobilappcaseemarket.ui.home.favourite

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mobilappcaseemarket.data.local.AppDatabase
import com.example.mobilappcaseemarket.data.repository.FavouriteRepository
import com.example.mobilappcaseemarket.data.repository.FavouriteRepositoryInterface
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FavouriteViewModel(
    private val repository: FavouriteRepositoryInterface
) : ViewModel() {

    private  val _favourites = MutableLiveData<Set<String>>()
    val favourites: LiveData<Set<String>> = _favourites


    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    init {
        loadFavourites()
    }

    fun loadFavourites() {
        viewModelScope.launch {
            try {
                _isLoading.value = true     // loading başladı
                delay(2000)
                val favList = repository.getAllFavourites()
                _favourites.value = favList.map { it.productId }.toSet()

            } finally {
                _isLoading.value = false    // loading bitti
            }
        }
    }



    fun toggleFavourite(productId: String) {
        viewModelScope.launch {
            val current = favourites.value ?: emptySet()

            if (current.contains(productId)) {
                repository.removeFromFavourite(productId)
                _favourites.value = current - productId
            } else {
                repository.addToFavourite(productId)
                _favourites.value = current + productId
            }
        }
    }



    class FavouriteViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            val dao = AppDatabase.getDatabase(context).favouriteDao()
            val repo = FavouriteRepository(dao)
            return FavouriteViewModel(repo) as T
        }
    }

}