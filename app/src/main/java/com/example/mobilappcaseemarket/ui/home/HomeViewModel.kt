package com.example.mobilappcaseemarket.ui.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilappcaseemarket.data.model.Product
import com.example.mobilappcaseemarket.data.repository.ProductRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: ProductRepository) : ViewModel() {

    val productList = MutableLiveData<MutableList<Product>>(mutableListOf())
    val isLoading = MutableLiveData<Boolean>()

    private var allProducts: List<Product> = emptyList()
    private var currentIndex = 0
    private val pageSize = 4
    private var isLastPage = false


    // ✔ fetchProducts KALDI, ama artık sadece TÜM ürünleri alıyor
    fun fetchProducts() {
        viewModelScope.launch {
            try {
                isLoading.value = true
                Log.d("VM_FETCH", "Tüm ürünler API'den çekiliyor...")
                allProducts = repository.getProducts()  // tüm ürünleri çek
                Log.d("VM_FETCH", "API'den toplam ürün: ${allProducts.size}")

                // İlk sayfayı yükle
                loadNextPage()

            } catch (e: Exception) {
                Log.e("VM_ERROR", "Ürünler alınırken hata oluştu: ${e.message}")
                e.printStackTrace()
            } finally {
                Log.d("VM_FETCH", "fetchProducts() tamamlandı.")
                isLoading.value = false
            }
        }
    }


    // 🎯 Scroll oldukça 8’er 8’er ekleyen fonksiyon
    fun loadNextPage() {
        if (isLastPage){
            Log.d("VM_PAGING", "Son sayfaya ulaşıldı, daha fazla ürün yok.")
            return
        }

        viewModelScope.launch {
            Log.d("VM_PAGING", "Yeni sayfa için delay başlıyor...")

            // ⏳ 1 saniye loading efekti
            delay(1000)
            Log.d("VM_PAGING", "bekliyor")

            val nextIndex = (currentIndex + pageSize).coerceAtMost(allProducts.size)
            Log.d("VM_PAGING", "Sayfa yükleniyor... currentIndex=$currentIndex → nextIndex=$nextIndex")

            val nextChunk = allProducts.subList(currentIndex, nextIndex)
            Log.d("VM_PAGING", "Bu sayfada yüklenecek ürün sayısı: ${nextChunk.size}")

            val currentList = productList.value ?: mutableListOf()
            currentList.addAll(nextChunk)
            productList.value = currentList

            Log.d("VM_PAGING", "Toplam gösterilen ürün sayısı: ${currentList.size}")

            currentIndex = nextIndex

            if (currentIndex >= allProducts.size) {
                isLastPage = true
                Log.d("VM_PAGING", "TÜM ürünler yüklendi. Son sayfadasın 🎉")
            }
        }
    }

}
