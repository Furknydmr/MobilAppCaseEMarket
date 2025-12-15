package com.example.mobilappcaseemarket.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient { //Bu bir Singleton. ani uygulama boyunca tek bir instance olacak.

    private const val BASE_URL = "https://5fc9346b2af77700165ae514.mockapi.io/" //API adresinin ana adresi.
//by lazy Bu Retrofit instance’ı yalnızca İLK kullanıldığında oluşturuluyor. Daha önce oluşturulmuşsa tekrar oluşturulmuyor.
    val api: ProductApi by lazy { //Retrofit nesnesi sadece İLK çalıştırıldığında oluşturulur. Daha önce oluşturulmuşsa tekrar yaratılmaz. Bu da performans demek.
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            //API’den gelen JSON verisini Kotlin data classlarına otomatik çevirmek için gerekli.
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProductApi::class.java)
    }
}