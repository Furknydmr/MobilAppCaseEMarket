package com.example.mobilappcaseemarket.repository

import com.example.mobilappcaseemarket.data.FakeCartDao
import com.example.mobilappcaseemarket.data.model.CartItem
import com.example.mobilappcaseemarket.data.model.Product
import com.example.mobilappcaseemarket.data.repository.CartRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class CartRepositoryTest {

    private val fakeDao = FakeCartDao()
    private val repo = CartRepository(fakeDao)

    @Test
    fun addToCart() = runBlocking {
        val product1 = Product(
            id = "1",
            name = "Apple",
            price = "10",
            image = "1",
            description = "Fresh red apple",
            model = "A1",
            brand = "FruitBrand",
            createdAt = "2024-01-01"
        )


        repo.addToCart(product1)
        val result = repo.getCartItems()

        assertEquals(1, result.size)
        assertEquals("iPhone", result[0].name)
        assertEquals("1000", result[0].price)
        assertEquals(1, result[0].quantity)
    }

    // 2) increaseQuantity -> adet artırılıyor mu?
    @Test
    fun increaseQuantity_shouldIncrease() = runBlocking {
        val product1 = Product(
            id = "1",
            name = "Apple",
            price = "10",
            image = "1",
            description = "Fresh red apple",
            model = "A1",
            brand = "FruitBrand",
            createdAt = "2024-01-01"
        )
        val item = CartItem("1", "1000", "iPhone", 1)
        repo.addToCart(product1)

        repo.increaseQuantity(item)
        val result = repo.getCartItems()[0]

        assertEquals(2, result.quantity)
    }

    // 3) decreaseQuantity -> adet azaltılıyor mu?
    @Test
    fun decreaseQuantity_shouldDecrease() = runBlocking {
        val product1 = Product(
            id = "1",
            name = "Apple",
            price = "10",
            image = "1",
            description = "Fresh red apple",
            model = "A1",
            brand = "FruitBrand",
            createdAt = "2024-01-01"
        )
        val item = CartItem("1", "1000", "iPhone", 2)
        repo.addToCart(product1)

        repo.decreaseQuantity(item)
        val result = repo.getCartItems()[0]

        assertEquals(1, result.quantity)
    }

    // 4) decreaseQuantity -> adet 1 ise ürün silinmeli
    @Test
    fun decreaseQuantity_whenZero_shouldDelete() = runBlocking {
        val product1 = Product(
            id = "1",
            name = "Apple",
            price = "10",
            image = "1",
            description = "Fresh red apple",
            model = "A1",
            brand = "FruitBrand",
            createdAt = "2024-01-01"
        )
        val item = CartItem("1", "1000", "iPhone", 1)
        repo.addToCart(product1)

        repo.decreaseQuantity(item)
        val result = repo.getCartItems()

        assertEquals(0, result.size)
    }

    // 5) deleteItem -> doğrudan silme çalışıyor mu?
    @Test
    fun deleteItem_shouldRemoveItem() = runBlocking {
        val product1 = Product(
            id = "1",
            name = "Apple",
            price = "10",
            image = "1",
            description = "Fresh red apple",
            model = "A1",
            brand = "FruitBrand",
            createdAt = "2024-01-01"
        )
        val item = CartItem("1", "1000", "iPhone", 1)
        repo.addToCart(product1)

        repo.deleteItem(item)
        val result = repo.getCartItems()

        assertEquals(0, result.size)
    }
}
