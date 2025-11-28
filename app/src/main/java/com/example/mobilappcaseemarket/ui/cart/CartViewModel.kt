package com.example.mobilappcaseemarket.ui.cart

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mobilappcaseemarket.data.local.AppDatabase
import com.example.mobilappcaseemarket.data.model.CartItem
import com.example.mobilappcaseemarket.data.model.Product
import com.example.mobilappcaseemarket.data.repository.CartRepository
import com.example.mobilappcaseemarket.data.repository.CartRepositoryInterface
import kotlinx.coroutines.launch

class CartViewModel(
    private val repository: CartRepositoryInterface
) : ViewModel() {

    private val _cartItems = MutableLiveData<List<CartItem>>()
    val cartItems: LiveData<List<CartItem>> = _cartItems

    fun loadCart() {
        viewModelScope.launch {
            val data = repository.getCartItems()

            _cartItems.value = repository.getCartItems()
        }
    }

    fun addToCart(item: CartItem) {

        viewModelScope.launch {

            repository.addToCart(item)
            loadCart()
        }
    }

    fun increaseQuantity(item: CartItem) {
        viewModelScope.launch {
            repository.increaseQuantity(item)
            loadCart()
        }
    }

    fun decreaseQuantity(item: CartItem) {
        viewModelScope.launch {
            repository.decreaseQuantity(item)
            loadCart()
        }
    }

    fun deleteItem(item: CartItem) {
        viewModelScope.launch {
            repository.deleteItem(item)
            loadCart()
        }
    }

    fun addProductToCart(product: Product) {
        viewModelScope.launch {

            val currentItems = repository.getCartItems()

            val existingItem = currentItems.find { it.id == product.id }

            if (existingItem != null) {

                repository.increaseQuantity(existingItem)
                loadCart()

                } else {
                val item = CartItem(
                    id = product.id,
                    name = product.name,
                    price = product.price,
                    quantity = 1
                )

                repository.addToCart(item)
                loadCart()
            }
        }
    }

    class CartViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {

            val dao = AppDatabase.getDatabase(context).cartDao()
            val repo = CartRepository(dao)

            return CartViewModel(repo) as T
        }
    }

}
