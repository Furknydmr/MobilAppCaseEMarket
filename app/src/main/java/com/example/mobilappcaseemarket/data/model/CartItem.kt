package com.example.mobilappcaseemarket.data.model

import android.R
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItem(
    @PrimaryKey(autoGenerate = false)
    val id: String, // product id
    val price: String,
    val name: String,
    var quantity: Int = 1
)
