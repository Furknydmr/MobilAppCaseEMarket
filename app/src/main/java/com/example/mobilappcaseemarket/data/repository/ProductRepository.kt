package com.example.mobilappcaseemarket.data.repository
import com.example.mobilappcaseemarket.data.model.Product
import com.example.mobilappcaseemarket.data.remote.RetrofitClient
import com.example.mobilappcaseemarket.data.remote.RetrofitClient.api

class ProductRepository {
    suspend fun getProducts() = RetrofitClient.api.getProducts()

    suspend fun getProductById(productId: String): Product {
        val allProducts = api.getProducts()

        return allProducts.find { it.id == productId }
            ?: throw Exception("Ürün bulunamadı: $productId")
    }

}

