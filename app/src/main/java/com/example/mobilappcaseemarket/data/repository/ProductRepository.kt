package com.example.mobilappcaseemarket.data.repository

import com.example.mobilappcaseemarket.data.model.Product
import com.example.mobilappcaseemarket.data.remote.ProductApi
import com.example.mobilappcaseemarket.data.remote.RetrofitClient
//zincirinde API çağrılarını tek noktaya toplamak.
class ProductRepository (private val api: ProductApi) : ProductRepositoryInterface {


    override suspend fun getProducts(): List<Product> {
        return try{ RetrofitClient.api.getProducts()
        } catch (e: Exception){
            throw Exception("The products could not be found.:${e.message}")
        }
    }

    override suspend fun getProductById(productId: String): Product {
        return try {
            RetrofitClient.api.getProductById(productId)
        } catch (e: Exception) {
            throw Exception("The product could not be brought.: ${e.message}")
        }
    }

}
