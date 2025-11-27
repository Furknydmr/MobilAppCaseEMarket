package com.example.mobilappcaseemarket.data.repository

import android.util.Log
import com.example.mobilappcaseemarket.data.local.CartDao
import com.example.mobilappcaseemarket.data.model.CartItem

class CartRepository(private val dao: CartDao) {

    // Sepetteki ürünleri getir
    suspend fun getCartItems(): List<CartItem> {
        return dao.getCartItems()
    }

    // Sepete ürün ekle
    suspend fun addToCart(item: CartItem) {
        Log.d("CART_REPO", "➡️ DAO.addToCart çağrılıyor... item=$item")
        dao.addToCart(item)
        Log.d("CART_REPO", "✔️ DAO.addToCart başarılı (Room'a yazıldı)")
    }

    // Miktarı artır
    suspend fun increaseQuantity(item: CartItem) {
        dao.updateQuantity(item.id, item.quantity + 1)
    }

    // Miktarı azalt (0 olursa sil)
    suspend fun decreaseQuantity(item: CartItem) {
        val newQty = item.quantity - 1
        if (newQty > 0) {
            dao.updateQuantity(item.id, newQty)
        } else {
            dao.deleteItem(item.id)
        }
    }

    // İsteyen ürünü direkt sil
    suspend fun deleteItem(item: CartItem) {
        dao.deleteItem(item.id)
    }
}
