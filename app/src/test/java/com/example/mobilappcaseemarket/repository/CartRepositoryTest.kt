package com.example.mobilappcaseemarket.data.repository

import com.example.mobilappcaseemarket.data.FakeCartDao
import com.example.mobilappcaseemarket.data.model.CartItem
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class CartRepositoryTest {

    private val fakeDao = FakeCartDao()
    private val repo = CartRepository(fakeDao)

    // 1) addToCart -> ürün ekleniyor mu?
    @Test
    fun addToCart_shouldAddItem() = runBlocking {
        val item = CartItem(id = "1", price = "1000", name = "iPhone", quantity = 1)

        repo.addToCart(item)
        val result = repo.getCartItems()

        assertEquals(1, result.size)
        assertEquals("iPhone", result[0].name)
        assertEquals("1000", result[0].price)
        assertEquals(1, result[0].quantity)
    }

    // 2) increaseQuantity -> adet artırılıyor mu?
    @Test
    fun increaseQuantity_shouldIncrease() = runBlocking {
        val item = CartItem("1", "1000", "iPhone", 1)
        repo.addToCart(item)

        repo.increaseQuantity(item)
        val result = repo.getCartItems()[0]

        assertEquals(2, result.quantity)
    }

    // 3) decreaseQuantity -> adet azaltılıyor mu?
    @Test
    fun decreaseQuantity_shouldDecrease() = runBlocking {
        val item = CartItem("1", "1000", "iPhone", 2)
        repo.addToCart(item)

        repo.decreaseQuantity(item)
        val result = repo.getCartItems()[0]

        assertEquals(1, result.quantity)
    }

    // 4) decreaseQuantity -> adet 1 ise ürün silinmeli
    @Test
    fun decreaseQuantity_whenZero_shouldDelete() = runBlocking {
        val item = CartItem("1", "1000", "iPhone", 1)
        repo.addToCart(item)

        repo.decreaseQuantity(item)
        val result = repo.getCartItems()

        assertEquals(0, result.size)
    }

    // 5) deleteItem -> doğrudan silme çalışıyor mu?
    @Test
    fun deleteItem_shouldRemoveItem() = runBlocking {
        val item = CartItem("1", "1000", "iPhone", 1)
        repo.addToCart(item)

        repo.deleteItem(item)
        val result = repo.getCartItems()

        assertEquals(0, result.size)
    }
}
