package com.example.mobilappcaseemarket.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mobilappcaseemarket.data.model.CartItem

@Dao
interface CartDao {

    @Query("SELECT * FROM cart_items")
    suspend fun getCartItems(): List<CartItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToCart(item: CartItem)

    @Query("UPDATE cart_items SET quantity = :qty WHERE id = :id")
    suspend fun updateQuantity(id: String, qty: Int)

    @Query("DELETE FROM cart_items WHERE id = :id")
    suspend fun deleteItem(id: String)
}