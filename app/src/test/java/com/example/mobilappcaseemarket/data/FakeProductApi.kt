package com.example.mobilappcaseemarket.data

import com.example.mobilappcaseemarket.data.model.Product
import com.example.mobilappcaseemarket.data.remote.ProductApi

class FakeProductApi : ProductApi {

    private val fakeProducts = listOf(
        Product("1", "iPhone", "image1", "1000", "desc", "model1", "Apple", "2024"),
        Product("2", "Samsung", "image2", "900", "desc", "model2", "Samsung", "2024")
    )

    override suspend fun getProducts(): List<Product> {
        return fakeProducts
    }

    override suspend fun getProductById(id: String): Product {
        TODO("Not yet implemented")
    }
}
