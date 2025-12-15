package com.example.mobilappcaseemarket.data.remote

import com.example.mobilappcaseemarket.data.model.Product
import retrofit2.http.GET
import retrofit2.http.Path


//Retrofit senin yerini kod yazan bir kütüphane.
//Sen sadece “böyle bir istek olacak” diyorsun → O arkada gerçek network request yazıyor.
interface ProductApi {

    @GET("products")
    suspend fun getProducts(): List<Product>

    @GET("products/{id}")
    suspend fun getProductById(
        @Path("id") id: String
    ): Product
}
