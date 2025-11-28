package com.example.mobilappcaseemarket.data.repository

import com.example.mobilappcaseemarket.data.local.CartDao
import com.example.mobilappcaseemarket.data.model.CartItem

class CartRepository(
    private val dao: CartDao
) : CartRepositoryInterface {

    override suspend fun getCartItems(): List<CartItem> {
        return dao.getCartItems()
    }

    override suspend fun addToCart(item: CartItem) {
        dao.addToCart(item)
    }

    override suspend fun increaseQuantity(item: CartItem) {
        dao.updateQuantity(item.id, item.quantity + 1)
    }

    override suspend fun decreaseQuantity(item: CartItem) {
        val newQty = item.quantity - 1
        if (newQty > 0) {
            dao.updateQuantity(item.id, newQty)
        } else {
            dao.deleteItem(item.id)
        }
    }

    override suspend fun deleteItem(item: CartItem) {
        dao.deleteItem(item.id)
    }
}
