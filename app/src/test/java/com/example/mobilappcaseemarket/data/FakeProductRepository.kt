package com.example.mobilappcaseemarket.data

import com.example.mobilappcaseemarket.data.remote.ProductApi
import com.example.mobilappcaseemarket.data.model.Product

class FakeProductRepository(private val api: ProductApi) {

    suspend fun getProducts() = api.getProducts()

    suspend fun getProductById(id: String): Product {
        return api.getProducts().find { it.id == id }
            ?: throw Exception("Not found")
    }
}
