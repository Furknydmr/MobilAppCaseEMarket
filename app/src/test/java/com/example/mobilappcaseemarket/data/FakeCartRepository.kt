package com.example.mobilappcaseemarket.data

import com.example.mobilappcaseemarket.data.model.CartItem
import com.example.mobilappcaseemarket.data.repository.CartRepositoryInterface

class FakeCartRepository : CartRepositoryInterface {

    private val cartItems = mutableListOf<CartItem>()

    override suspend fun getCartItems(): List<CartItem> {
        return cartItems.toList()
    }

    override suspend fun addToCart(item: CartItem) {
        cartItems.add(item)
    }

    override suspend fun increaseQuantity(item: CartItem) {
        val index = cartItems.indexOfFirst { it.id == item.id }
        if (index != -1) {
            val current = cartItems[index]
            cartItems[index] = current.copy(quantity = current.quantity + 1)
        }
    }

    override suspend fun decreaseQuantity(item: CartItem) {
        val index = cartItems.indexOfFirst { it.id == item.id }
        if (index != -1) {
            val current = cartItems[index]
            val newQty = current.quantity - 1

            if (newQty > 0) {
                cartItems[index] = current.copy(quantity = newQty)
            } else {
                cartItems.removeAt(index)
            }
        }
    }

    override suspend fun deleteItem(item: CartItem) {
        cartItems.removeIf { it.id == item.id }
    }
}

