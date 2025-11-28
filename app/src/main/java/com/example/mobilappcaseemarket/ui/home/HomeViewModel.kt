package com.example.mobilappcaseemarket.ui.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilappcaseemarket.data.model.Product
import com.example.mobilappcaseemarket.data.repository.ProductRepositoryInterface
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: ProductRepositoryInterface) : ViewModel() {

    val productList = MutableLiveData<List<Product>>()
    val isLoading = MutableLiveData<Boolean>()

    private var allProducts: List<Product> = emptyList()
    private var filteredProducts: List<Product> = emptyList()

    var filterOptions = FilterOptions()

    private var currentIndex = 0
    private val pageSize = 10
    private var isLastPage = false



    fun fetchProducts() {
        viewModelScope.launch {
            try {
                isLoading.value = true
                allProducts = repository.getProducts()
                applyFilters()
            } finally {
                isLoading.value = false
            }
        }
    }



    fun updateFilter(newOptions: FilterOptions) {
        filterOptions = newOptions
        applyFilters()
    }



    private fun applyFilters() {
        var result = allProducts

        // 🔍 Search
        if (filterOptions.searchQuery.isNotBlank()) {
            result = result.filter {
                it.name.contains(filterOptions.searchQuery, ignoreCase = true)
            }
        }


        result = when (filterOptions.sortType) {
            SortType.PRICE_ASC -> result.sortedBy { it.price.toFloat() }
            SortType.PRICE_DESC -> result.sortedByDescending { it.price.toFloat() }
            SortType.NAME_ASC -> result.sortedBy { it.name }
            SortType.NAME_DESC -> result.sortedByDescending { it.name }
            else -> result
        }

        filteredProducts = result
        resetAndLoad()
    }



    private fun resetAndLoad() {
        currentIndex = 0
        isLastPage = false
        productList.value = emptyList()
        loadNextPage()
    }

    fun loadNextPage() {
        if (isLastPage) return

        val nextIndex = (currentIndex + pageSize).coerceAtMost(filteredProducts.size)
        val nextChunk = filteredProducts.subList(currentIndex, nextIndex)

        val updated = (productList.value ?: emptyList()) + nextChunk
        productList.value = updated

        currentIndex = nextIndex

        if (currentIndex >= filteredProducts.size) {
            isLastPage = true
        }
    }
}
