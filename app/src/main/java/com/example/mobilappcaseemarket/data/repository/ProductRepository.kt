package com.example.mobilappcaseemarket.data.repository

import com.example.mobilappcaseemarket.data.model.Product
import com.example.mobilappcaseemarket.data.remote.RetrofitClient

class ProductRepository : ProductRepositoryInterface {

    override suspend fun getProducts(): List<Product> {
        return RetrofitClient.api.getProducts()
    }

    override suspend fun getProductById(productId: String): Product {
        val allProducts = RetrofitClient.api.getProducts()
        return allProducts.find { it.id == productId }
            ?: throw Exception("Ürün bulunamadı: $productId")
    }
}
