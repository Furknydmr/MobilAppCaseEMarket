package com.example.mobilappcaseemarket.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilappcaseemarket.data.model.Product
import com.example.mobilappcaseemarket.data.repository.ProductRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: ProductRepository) : ViewModel() {

    val  productList = MutableLiveData<List<Product>>()
    val isLoading = MutableLiveData<Boolean>()

    fun fetchProducts() {
        viewModelScope.launch {
            try {
                isLoading.value = true
                val response = repository.getProducts()
                productList.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading.value = false
            }
        }
    }
}