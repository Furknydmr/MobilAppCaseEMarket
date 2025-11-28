package com.example.mobilappcaseemarket.data

class FakeCartRepository : CartRepositoryInterface {

    private val cartItems = mutableListOf<CartItem>()

    override suspend fun getCartItems(): List<CartItem> {
        return cartItems.toList()
    }

    override suspend fun addToCart(item: CartItem) {
        cartItems.add(item)
    }

    override suspend fun increaseQuantity(item: CartItem) {
        val index = cartItems.indexOfFirst { it.id == item.id }
        if (index != -1) {
            val updated = cartItems[index].copy(quantity = cartItems[index].quantity + 1)
            cartItems[index] = updated
        }
    }

    override suspend fun decreaseQuantity(item: CartItem) {
        val index = cartItems.indexOfFirst { it.id == item.id }
        if (index != -1) {
            val current = cartItems[index]
            if (current.quantity > 1) {
                cartItems[index] = current.copy(quantity = current.quantity - 1)
            } else {
                cartItems.removeAt(index)
            }
        }
    }

    override suspend fun deleteItem(item: CartItem) {
        cartItems.removeIf { it.id == item.id }
    }
}
