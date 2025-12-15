package com.example.mobilappcaseemarket.data.repository

import com.example.mobilappcaseemarket.data.model.Product

interface ProductRepositoryInterface {
    suspend fun getProducts(): List<Product>

    suspend fun getProductById(productId: String): Product
}
