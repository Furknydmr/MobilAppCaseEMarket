package com.example.mobilappcaseemarket.data

import com.example.mobilappcaseemarket.data.local.CartDao
import com.example.mobilappcaseemarket.data.model.CartItem

class FakeCartDao : CartDao {

    private val cartItems = mutableListOf<CartItem>()

    override suspend fun getCartItems(): List<CartItem> {
        return cartItems
    }

    override suspend fun addToCart(item: CartItem) {
        val index = cartItems.indexOfFirst { it.id == item.id }
        if (index != -1) {
            cartItems[index] = item
        } else {
            cartItems.add(item)
        }
    }

    override suspend fun updateQuantity(id: String, qty: Int) {
        val item = cartItems.find { it.id == id }
        if (item != null) {
            item.quantity = qty
        }
    }

    override suspend fun deleteItem(id: String) {
        cartItems.removeIf { it.id == id }
    }
}
