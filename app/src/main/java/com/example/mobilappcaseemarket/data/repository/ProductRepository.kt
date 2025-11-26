package com.example.mobilappcaseemarket.data.repository
import com.example.mobilappcaseemarket.data.remote.RetrofitClient

class ProductRepository {
    suspend fun getProducts() = RetrofitClient.api.getProducts()
}