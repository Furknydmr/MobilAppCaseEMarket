package com.example.mobilappcaseemarket.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.mobilappcaseemarket.data.FakeCartRepository
import com.example.mobilappcaseemarket.data.model.CartItem
import com.example.mobilappcaseemarket.data.model.Product
import com.example.mobilappcaseemarket.ui.cart.CartViewModel
import com.example.mobilappcaseemarket.utils.getOrAwaitValue
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi //Kotlin’in test amaçlı ürettiği coroutine API’lerini (runTest, testDispatcher vs.) kullanıyorum.
class CartViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule() //LiveData güncellemelerini senkron hale getirir.


    private lateinit var viewModel: CartViewModel
    private lateinit var fakeRepo: FakeCartRepository

    @Before
    fun setup() {
        fakeRepo = FakeCartRepository()
        viewModel = CartViewModel(fakeRepo)
    }

    @Test
    fun `loadCart loads items successfully`() = runTest {
        fakeRepo.addToCart(Product(
            "1", "Apple", "10", "1",
            description = "description",
            model = "model",
            brand = "brand",
            createdAt = "createdAt"
        ))

        viewModel.loadCart()
        val result = viewModel.cartItems.getOrAwaitValue()

        assertEquals(1, result.size)
        assertEquals("Apple", result[0].name)
    }

    @Test
    fun addToCart_whenItemDoesNotExist_shouldAddNewItem() = runTest {
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
        val product2 = Product(
            id = "2",
            name = "Banana",
            price = "12",
            image = "2",
            description = "Organic ripe banana",
            model = "B2",
            brand = "TropicalBrand",
            createdAt = "2024-01-02"
        )



        fakeRepo.addToCart(product1)
        fakeRepo.addToCart(product2)

        val result = fakeRepo.getCartItems()

        assertEquals(2, result.size)
        assertEquals(1, result[0].quantity)
    }

    @Test
    fun addToCart_whenItemExists_shouldIncreaseQuantity() = runTest {
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


        fakeRepo.addToCart(product1)
        fakeRepo.addToCart(product1)

        val result = fakeRepo.getCartItems()[0]

        assertEquals(2, result.quantity)
    }



    @Test
    fun `increaseQuantity increments correctly`() = runTest {
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
        val item = CartItem("1", "Apple", "10", 1)
        fakeRepo.addToCart(product1)
        viewModel.increaseQuantity(item)
        val result = viewModel.cartItems.getOrAwaitValue()

        assertEquals(2, result[0].quantity)
    }

    @Test
    fun `decreaseQuantity decrements correctly`() = runTest {
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
        fakeRepo.addToCart(product1)
        fakeRepo.addToCart(product1)
        val item = CartItem("1", "Apple", "10", 2)

        viewModel.decreaseQuantity(item)
        val result = viewModel.cartItems.getOrAwaitValue()

        assertEquals(1, result[0].quantity)
    }

    @Test
    fun `decreaseQuantity removes when qty goes to zero`() = runTest {
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
        val item = CartItem("1", "Apple", "10", 1)
        fakeRepo.addToCart(product1)

        viewModel.decreaseQuantity(item)
        val result = viewModel.cartItems.getOrAwaitValue()

        assertTrue(result.isEmpty())
    }

    @Test
    fun `deleteItem removes product`() = runTest {
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
        val item = CartItem("1", "Apple", "10", 1)
        fakeRepo.addToCart(product1)

        viewModel.deleteItem(item)
        val result = viewModel.cartItems.getOrAwaitValue()

        assertEquals(0, result.size)
    }
}
