package com.example.mobilappcaseemarket.viewmodel

import ProductDetailViewModel
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.mobilappcaseemarket.data.FakeProductDetailRepository
import com.example.mobilappcaseemarket.data.model.Product
import com.example.mobilappcaseemarket.utils.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProductDetailViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: ProductDetailViewModel
    private lateinit var fakeRepo: FakeProductDetailRepository

    private val fakeProducts = listOf(
        Product("1", "iPhone 14", "img1", "50000", "desc", "X", "Apple", "2024"),
        Product("2", "Samsung S24", "img2", "40000", "desc", "S24", "Samsung", "2024")
    )

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        fakeRepo = FakeProductDetailRepository(fakeProducts)
        viewModel = ProductDetailViewModel(fakeRepo)
    }

    // 1) fetchProductById doğru ürünü getiriyor mu?
    @Test
    fun fetchProductById_shouldLoadCorrectProduct() {
        viewModel.fetchProductById("1")

        val result = viewModel.product.getOrAwaitValue()

        assertEquals("iPhone 14", result.name)
        assertEquals("50000", result.price)
    }

    // 2) Geçersiz ID exception fırlatıyor mu?
    @Test
    fun fetchProductById_invalidId_shouldNotSetProduct() {
        try {
            viewModel.fetchProductById("999")
        } catch (_: Exception) {
            // beklenen
        }

        val result = viewModel.product.value
        assertNull(result)
    }

}
