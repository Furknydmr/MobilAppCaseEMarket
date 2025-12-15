package com.example.mobilappcaseemarket.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.mobilappcaseemarket.data.model.Product
import com.example.mobilappcaseemarket.data.remote.RetrofitClient
import com.example.mobilappcaseemarket.data.repository.ProductRepository
import com.example.mobilappcaseemarket.data.repository.ProductRepositoryInterface
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: ProductRepositoryInterface) : ViewModel() {


    private val _productList = MutableLiveData<List<Product>>()
    val productList: LiveData<List<Product>> get() = _productList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading


    //API’dan gelen ham veri. UI’ın görmediği saf liste. Sadece filtre ve arama işlemleri için kullanılıyor
    private var allProducts: List<Product> = emptyList()
    private var filteredProducts: List<Product> = emptyList()

    var filterOptions = FilterOptions()

    private var currentIndex = 0
    private val pageSize = 4
    private var isLastPage = false



    fun fetchProducts() {
        viewModelScope.launch { //Bu, suspend fonksiyonları çalıştırmak için coroutine açıyor. UI donmaz. Network beklerken ekran akıcı kalır.
            try {
                _isLoading.value = true //Veri çekiyorum, bir saniye bekle.
                allProducts = repository.getProducts() //API çağrısı
                applyFilters()
            } finally {
                _isLoading.value = false //İş bitti, loading’i kapat
            }
        }
    }



    fun updateFilter(newOptions: FilterOptions) {
        filterOptions = newOptions
        applyFilters()
    }



    private fun applyFilters() {
        var result = allProducts

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
        _productList.value = emptyList()
        loadNextPage()
    }

    fun loadNextPage() {
        if (isLastPage) return

        val nextIndex = (currentIndex + pageSize).coerceAtMost(filteredProducts.size)
        val nextChunk = filteredProducts.subList(currentIndex, nextIndex)

        val updated = (productList.value ?: emptyList()) + nextChunk
        _productList.value = updated

        currentIndex = nextIndex

        if (currentIndex >= filteredProducts.size) {
            isLastPage = true
        }
    }

    class HomeViewModelFactory : ViewModelProvider.Factory {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {

            val api = RetrofitClient.api
            val repo = ProductRepository(api)

            return HomeViewModel(repo) as T
        }
    }


}
