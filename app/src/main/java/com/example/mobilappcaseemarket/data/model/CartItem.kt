package com.example.mobilappcaseemarket.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItem(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val name: String,
    val price: String,
    var quantity: Int = 1
)
