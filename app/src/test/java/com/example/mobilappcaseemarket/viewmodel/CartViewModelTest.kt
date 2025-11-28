package com.example.mobilappcaseemarket.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.mobilappcaseemarket.data.FakeCartRepository
import com.example.mobilappcaseemarket.data.model.CartItem
import com.example.mobilappcaseemarket.ui.cart.CartViewModel
import com.example.mobilappcaseemarket.utils.getOrAwaitValue
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CartViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()


    private lateinit var viewModel: CartViewModel
    private lateinit var fakeRepo: FakeCartRepository

    @Before
    fun setup() {
        fakeRepo = FakeCartRepository()
        viewModel = CartViewModel(fakeRepo)
    }

    @Test
    fun `loadCart loads items successfully`() = runTest {
        fakeRepo.addToCart(CartItem("1", "Apple", "10", 1))

        viewModel.loadCart()
        val result = viewModel.cartItems.getOrAwaitValue()

        assertEquals(1, result.size)
        assertEquals("Apple", result[0].name)
    }

    @Test
    fun `addToCart adds item correctly`() = runTest {
        val item = CartItem("1", "Item", "20", 1)

        viewModel.addToCart(item)
        val result = viewModel.cartItems.getOrAwaitValue()

        assertEquals(1, result.size)
        assertEquals("Item", result[0].name)
    }

    @Test
    fun `increaseQuantity increments correctly`() = runTest {
        val item = CartItem("1", "Mouse", "50", 1)
        fakeRepo.addToCart(item)

        viewModel.increaseQuantity(item)
        val result = viewModel.cartItems.getOrAwaitValue()

        assertEquals(2, result[0].quantity)
    }

    @Test
    fun `decreaseQuantity decrements correctly`() = runTest {
        val item = CartItem("1", "Keyboard", "100", 2)
        fakeRepo.addToCart(item)

        viewModel.decreaseQuantity(item)
        val result = viewModel.cartItems.getOrAwaitValue()

        assertEquals(1, result[0].quantity)
    }

    @Test
    fun `decreaseQuantity removes when qty goes to zero`() = runTest {
        val item = CartItem("1", "Keyboard", "100", 1)
        fakeRepo.addToCart(item)

        viewModel.decreaseQuantity(item)
        val result = viewModel.cartItems.getOrAwaitValue()

        assertTrue(result.isEmpty())
    }

    @Test
    fun `deleteItem removes product`() = runTest {
        val item = CartItem("1", "Test", "30", 1)
        fakeRepo.addToCart(item)

        viewModel.deleteItem(item)
        val result = viewModel.cartItems.getOrAwaitValue()

        assertEquals(0, result.size)
    }
}
