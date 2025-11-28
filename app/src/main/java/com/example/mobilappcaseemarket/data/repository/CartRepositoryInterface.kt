package com.example.mobilappcaseemarket.data.repository

import com.example.mobilappcaseemarket.data.model.CartItem

interface CartRepositoryInterface {

    suspend fun getCartItems(): List<CartItem>

    suspend fun addToCart(item: CartItem)

    suspend fun increaseQuantity(item: CartItem)

    suspend fun decreaseQuantity(item: CartItem)

    suspend fun deleteItem(item: CartItem)
}
