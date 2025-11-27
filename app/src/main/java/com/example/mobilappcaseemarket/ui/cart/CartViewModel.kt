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
import kotlinx.coroutines.launch

class CartViewModel(
    private val repository: CartRepository
) : ViewModel() {

    private val _cartItems = MutableLiveData<List<CartItem>>()
    val cartItems: LiveData<List<CartItem>> = _cartItems

    // İlk çalıştırıldığında sepeti yükle
    fun loadCart() {
        viewModelScope.launch {
            val data = repository.getCartItems()
            Log.d("CART_DEBUG", "ROOM'DAN GELEN ÜRÜN SAYISI: ${data.size}")
            _cartItems.value = repository.getCartItems()
        }
    }

    // Sepete ekleme işlemi (Hem Home hem Detail ekranı buraya bağlanacak)
    fun addToCart(item: CartItem) {

        viewModelScope.launch {
            Log.d("CART_VM", "📥 addToCart() çağrıldı → item = $item")
            repository.addToCart(item)
            Log.d("CART_VM", "💾 Repository.addToCart tamamlandı")
            loadCart() // güncel listeyi yükle
        }
    }


    // Ürün miktarını artır
    fun increaseQuantity(item: CartItem) {
        viewModelScope.launch {
            repository.increaseQuantity(item)
            loadCart()
        }
    }

    // Ürün miktarını azalt
    fun decreaseQuantity(item: CartItem) {
        viewModelScope.launch {
            repository.decreaseQuantity(item)
            loadCart()
        }
    }

    // Ürünü tamamen sil
    fun deleteItem(item: CartItem) {
        viewModelScope.launch {
            repository.deleteItem(item)
            loadCart()
        }
    }

    fun addProductToCart(product: Product) {
        viewModelScope.launch {

            // Sepetteki ürünleri al
            val currentItems = repository.getCartItems()

            // Bu ürün zaten var mı?
            val existingItem = currentItems.find { it.id == product.id }

            if (existingItem != null) {
                // 🔥 Ürün zaten sepette → quantity +1
                repository.increaseQuantity(existingItem)
                loadCart()   // güncel listeyi yay
                Log.d("CART_VM", "🔄 Ürün zaten var → quantity +1 yapıldı: ${existingItem.id}")
            } else {
                // 🆕 Ürün yok → yeni ekle
                val item = CartItem(
                    id = product.id,
                    name = product.name,
                    price = product.price,
                    quantity = 1
                )

                repository.addToCart(item)
                loadCart()
                Log.d("CART_VM", "🆕 Yeni ürün sepete eklendi: ${item.id}")
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
