package com.example.mobilappcaseemarket.data

import com.example.mobilappcaseemarket.data.model.CartItem
import com.example.mobilappcaseemarket.data.model.Product
import com.example.mobilappcaseemarket.data.repository.CartRepositoryInterface

class FakeCartRepository : CartRepositoryInterface {

    private val cartItems = mutableListOf<CartItem>()

    override suspend fun getCartItems(): List<CartItem> {
        return cartItems.toList()
    }

    override suspend fun addToCart(product: Product) {

        val existingItem = cartItems.find { it.id == product.id }

        if (existingItem != null) {
            val updated = existingItem.copy(quantity = existingItem.quantity + 1)
            val index = cartItems.indexOf(existingItem)
            cartItems[index] = updated

        } else {
            val newItem = CartItem(
                id = product.id,
                name = product.name,
                price = product.price,
                quantity = 1
            )
            cartItems.add(newItem)
        }
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

