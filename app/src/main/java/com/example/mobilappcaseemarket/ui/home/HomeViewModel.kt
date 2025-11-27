package com.example.mobilappcaseemarket.ui.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilappcaseemarket.data.model.Product
import com.example.mobilappcaseemarket.data.repository.ProductRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: ProductRepository) : ViewModel() {

    // View’da gösterilecek ürün listesi
    val productList = MutableLiveData<List<Product>>()
    val isLoading = MutableLiveData<Boolean>()

    // API’den gelen tüm orijinal ürünler
    private var allProducts: List<Product> = emptyList()

    // Filtrelenmiş & sıralanmış ürünler
    private var filteredProducts: List<Product> = emptyList()

    private var currentIndex = 0
    private val pageSize = 10

    private var isLastPage = false


    // ************************************
    // STEP 1 — API’den ürünleri al (1 defa)
    // ************************************
    fun fetchProducts() {
        viewModelScope.launch {
            try {
                isLoading.value = true

                allProducts = repository.getProducts()
                filteredProducts = allProducts

                resetAndLoad()

            } finally {
                isLoading.value = false
            }
        }
    }


    // ************************************
    // STEP 2 — Pagination yapısı
    // ************************************
    fun loadNextPage() {

        Log.d("VM_PAGING", "loadNextPage ÇAĞIRILDI")
        Log.d("VM_PAGING", "currentIndex=$currentIndex size=${filteredProducts.size}")

        if (isLastPage) {
            Log.d("VM_PAGING", "❌ Son sayfa → yeni ürün yok")
            return
        }

        val nextIndex = (currentIndex + pageSize).coerceAtMost(filteredProducts.size)
        Log.d("VM_PAGING", "nextIndex=$nextIndex")

        val nextChunk = filteredProducts.subList(currentIndex, nextIndex)
        Log.d("VM_PAGING", "Yüklenen ürün sayısı: ${nextChunk.size}")

        val updatedList = (productList.value ?: emptyList()) + nextChunk
        productList.value = updatedList

        Log.d("VM_PAGING", "Toplam gösterilen ürün: ${updatedList.size}")

        currentIndex = nextIndex
        Log.d("VM_PAGING", "Yeni currentIndex: $currentIndex")

        if (currentIndex >= filteredProducts.size) {
            isLastPage = true
            Log.d("VM_PAGING", "🎉 TÜM ürünler yüklendi → isLastPage=true")
        }
    }



    private fun resetAndLoad() {
        currentIndex = 0
        isLastPage = false
        productList.value = emptyList()
        loadNextPage()
    }


    // ************************************
    // STEP 3 — Search (Local)
    // ************************************
    fun search(query: String) {
        filteredProducts = if (query.isBlank()) {
            allProducts
        } else {
            allProducts.filter {
                it.name.contains(query, ignoreCase = true)
            }
        }

        resetAndLoad()
    }


    // ************************************
    // STEP 4 — Sorting (Local)
    // ************************************
    fun sortByPriceAsc() {
        filteredProducts = filteredProducts.sortedBy { it.price.toFloat() }
        resetAndLoad()
    }

    fun sortByPriceDesc() {
        filteredProducts = filteredProducts.sortedByDescending { it.price.toFloat() }
        resetAndLoad()
    }

    fun sortByNameAZ() {
        filteredProducts = filteredProducts.sortedBy { it.name }
        resetAndLoad()
    }

    fun sortByNameZA() {
        filteredProducts = filteredProducts.sortedByDescending { it.name }
        resetAndLoad()
    }
}

