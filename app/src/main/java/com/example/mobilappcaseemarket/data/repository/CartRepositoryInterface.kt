package com.example.mobilappcaseemarket.data.repository

import com.example.mobilappcaseemarket.data.model.CartItem
import com.example.mobilappcaseemarket.data.model.Product


//Bu interface, Repository katmanının sözleşmesidir (contract).
//ViewModel bu fonksiyonların nasıl çalıştığını bilmez, yalnızca:
//“Ben bu fonksiyonları çağırırım, Repository de gereğini yapar.”
interface CartRepositoryInterface {

    suspend fun getCartItems(): List<CartItem>

    suspend fun addToCart(product: Product)

    suspend fun increaseQuantity(item: CartItem)

    suspend fun decreaseQuantity(item: CartItem)

    suspend fun deleteItem(item: CartItem)
}
