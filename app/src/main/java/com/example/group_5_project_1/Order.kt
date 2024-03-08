package com.example.group_5_project_1

data class Order(
    val userName: String = "",
    val userEmail: String = "",
    val userPhone: String = "",
    val orderDateTime: String = "",
    val cartItems: MutableList<CartItem> = mutableListOf()
) {
    fun addCartItem(cartItem: CartItem) {
        cartItems.add(cartItem)
    }
}

