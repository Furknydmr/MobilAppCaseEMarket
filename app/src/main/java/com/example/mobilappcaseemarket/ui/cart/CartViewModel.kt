package com.example.mobilappcaseemarket.ui.cart

import android.content.Context
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

    private val _cartCount = MutableLiveData<Int>()
    val cartCount: LiveData<Int> = _cartCount

    fun loadCart() {
        viewModelScope.launch {
            val data = repository.getCartItems()
            _cartItems.value = data
            _cartCount.value = data.sumOf { it.quantity }
        }
    }


    fun addProductToCart(product: Product) {
        viewModelScope.launch {
            repository.addToCart(product)
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

    fun calculateTotalPrice(items: List<CartItem>): Double {
        return items.sumOf { item ->
            val price = item.price.toDoubleOrNull() ?: 0.0
            price * item.quantity
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

