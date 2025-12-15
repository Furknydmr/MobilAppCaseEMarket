package com.example.mobilappcaseemarket.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mobilappcaseemarket.data.model.CartItem


//Veritabanına gönderilen SQL komutlarının tanımlandığı yer
@Dao
interface CartDao { //Bu interface veritabanı işlemleri içerecek.


    //Room compile-time’da bu interface’in gerçek implementasyonunu otomatik üretir.
    //Yani sen fonksiyon gövdesi yazmazsın, Room senin yerinize yazdığı sınıfı oluşturur.
    //Bu yüzden interface olmak zorunda.

    @Query("SELECT * FROM cart_items")
    suspend fun getCartItems(): List<CartItem>
    //Neden suspend?
    //Veritabanı IO operasyonudur
    //Main thread’de çalışamaz → ANR olur
    //Coroutines ile arka planda çalışmalı

    @Insert(onConflict = OnConflictStrategy.REPLACE) //Aynı ürünü tekrar eklemek istediğinde duplicate oluşmasın. ID üzerinden güncellensin
    suspend fun addToCart(item: CartItem)

    @Query("UPDATE cart_items SET quantity = :qty WHERE id = :id")
    suspend fun updateQuantity(id: String, qty: Int)

    @Query("DELETE FROM cart_items WHERE id = :id")
    suspend fun deleteItem(id: String)
}