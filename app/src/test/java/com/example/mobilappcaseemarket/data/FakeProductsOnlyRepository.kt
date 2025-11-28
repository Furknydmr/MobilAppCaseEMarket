package com.example.mobilappcaseemarket.data

import com.example.mobilappcaseemarket.data.model.Product
import com.example.mobilappcaseemarket.data.repository.ProductRepositoryInterface

class FakeProductsOnlyRepository(
    private val fakeList: List<Product>
) : ProductRepositoryInterface {

    override suspend fun getProducts(): List<Product> {
        return fakeList
    }

    override suspend fun getProductById(productId: String): Product {
        return fakeList.find { it.id == productId }
            ?: throw Exception("Product not found")
    }
}
