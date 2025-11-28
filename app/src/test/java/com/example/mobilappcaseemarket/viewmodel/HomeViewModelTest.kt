package com.example.mobilappcaseemarket.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.mobilappcaseemarket.data.FakeProductsOnlyRepository
import com.example.mobilappcaseemarket.data.model.Product
import com.example.mobilappcaseemarket.data.repository.ProductRepositoryInterface
import com.example.mobilappcaseemarket.ui.home.FilterOptions
import com.example.mobilappcaseemarket.ui.home.HomeViewModel
import com.example.mobilappcaseemarket.ui.home.SortType
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
class HomeViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: HomeViewModel
    private lateinit var fakeRepo: ProductRepositoryInterface

    private val fakeProducts = listOf(
        Product("1", "Apple iPhone", "img1", "1000", "desc", "X", "Apple", "2024"),
        Product("2", "Samsung S24", "img2", "800", "desc", "S24", "Samsung", "2024"),
        Product("3", "Xiaomi Note", "img3", "500", "desc", "Note", "Xiaomi", "2024")
    )

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        fakeRepo = FakeProductsOnlyRepository(fakeProducts)
        viewModel = HomeViewModel(fakeRepo)
    }

    @Test
    fun fetchProducts_shouldLoadInitialProducts() {
        viewModel.fetchProducts()

        val list = viewModel.productList.getOrAwaitValue()
        assertEquals(3, list.size)
        assertEquals("Apple iPhone", list[0].name)
    }

    @Test
    fun searchFilter_shouldReturnMatchingProduct() {
        viewModel.fetchProducts()

        viewModel.updateFilter(
            FilterOptions(searchQuery = "apple")
        )

        val result = viewModel.productList.getOrAwaitValue()
        assertEquals(1, result.size)
        assertEquals("Apple iPhone", result[0].name)
    }

    @Test
    fun sortFilter_priceDesc_shouldSortDescending() {
        viewModel.fetchProducts()

        viewModel.updateFilter(
            FilterOptions(sortType = SortType.PRICE_DESC)
        )

        val result = viewModel.productList.getOrAwaitValue()

        assertEquals("1000", result[0].price)
        assertEquals("800", result[1].price)
        assertEquals("500", result[2].price)
    }

    @Test
    fun paging_shouldNotDuplicateOrMissItems() {
        viewModel.fetchProducts()

        val firstPage = viewModel.productList.getOrAwaitValue()
        assertEquals(3, firstPage.size)

        viewModel.loadNextPage()

        val secondPage = viewModel.productList.getOrAwaitValue()
        assertEquals(3, secondPage.size)
    }

    @Test
    fun fetchProducts_shouldToggleIsLoading() {
        viewModel.fetchProducts()

        val loading = viewModel.isLoading.getOrAwaitValue()
        assertFalse(loading)
    }

    @Test
    fun sortFilter_nameAsc() {
        viewModel.fetchProducts()

        viewModel.updateFilter(
            FilterOptions(sortType = SortType.NAME_ASC)
        )

        val result = viewModel.productList.getOrAwaitValue()

        assertEquals("Apple iPhone", result[0].name)
        assertEquals("Samsung S24", result[1].name)
        assertEquals("Xiaomi Note", result[2].name)
    }

    @Test
    fun sortFilter_nameDesc() {
        viewModel.fetchProducts()

        viewModel.updateFilter(
            FilterOptions(sortType = SortType.NAME_DESC)
        )

        val result = viewModel.productList.getOrAwaitValue()

        assertEquals("Xiaomi Note", result[0].name)
        assertEquals("Samsung S24", result[1].name)
        assertEquals("Apple iPhone", result[2].name)
    }

    @Test
    fun sortFilter_priceAsc() {
        viewModel.fetchProducts()

        viewModel.updateFilter(
            FilterOptions(sortType = SortType.PRICE_ASC)
        )

        val result = viewModel.productList.getOrAwaitValue()

        assertEquals("500", result[0].price)
        assertEquals("800", result[1].price)
        assertEquals("1000", result[2].price)
    }

    @Test
    fun sortFilter_priceDesc() {
        viewModel.fetchProducts()

        viewModel.updateFilter(
            FilterOptions(sortType = SortType.PRICE_DESC)
        )

        val result = viewModel.productList.getOrAwaitValue()

        assertEquals("1000", result[0].price)  // en pahalı
        assertEquals("800", result[1].price)
        assertEquals("500", result[2].price)  // en ucuz
    }

    @Test
    fun searchFilter() {
        viewModel.fetchProducts()
        viewModel.updateFilter(
            FilterOptions(
                searchQuery = "sam"
            )
        )
        val result = viewModel.productList.getOrAwaitValue()
        assertEquals(1, result.size)
        assertEquals("Samsung S24", result[0].name)
    }




}
