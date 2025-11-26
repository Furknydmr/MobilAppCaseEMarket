package com.example.mobilappcaseemarket.data.remote

import com.example.mobilappcaseemarket.data.model.Product
import retrofit2.http.GET

interface ProductApi {

    @GET("products")
    suspend fun getProducts(): List<Product>
}