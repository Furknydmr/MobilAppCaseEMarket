package com.example.mobilappcaseemarket.data.repository

import com.example.mobilappcaseemarket.data.local.CartDao
import com.example.mobilappcaseemarket.data.model.CartItem
import com.example.mobilappcaseemarket.data.model.Product

class CartRepository(private val dao: CartDao) : CartRepositoryInterface {


    //ViewModel bana ‘sepetteki ürünleri getir’ dedi.
    //DAO’ya gidip SQL çalıştıracağım ve sonucu ViewModel’e geri vereceğim.”
    override suspend fun getCartItems(): List<CartItem> {
        return dao.getCartItems()
    }



    override suspend fun addToCart(product: Product) {

        // 1) Mevcut sepeti DB'den al
        val currentItems = dao.getCartItems()

        // 2) Eklenmek istenen ürün zaten var mı bak
        val existingItem = currentItems.find { it.id == product.id }

        if (existingItem != null) {
            // 3a) Sepette varsa quantity artır
            dao.updateQuantity(
                existingItem.id,
                existingItem.quantity + 1
            )

        } else {
            // 3b) Sepette yoksa yeni CartItem oluştur ve ekle
            val newItem = CartItem(
                id = product.id,
                name = product.name,
                price = product.price,
                quantity = 1
            )

            dao.addToCart(newItem)
        }
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
