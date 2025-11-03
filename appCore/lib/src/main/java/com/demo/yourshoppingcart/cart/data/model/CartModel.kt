package com.demo.yourshoppingcart.cart.data.model

abstract class CartModel {

    data class Cart(
        val cartId: String = "",
        val cartItem: List<CartItem> = emptyList()
    )

    data class CartItem(
        val productId: String = "",
        val productName: String = "",
        val productPrice: String = "",
        val productQun: Int = 0,
        val productDes: String = "",
        val productImg: String = ""
    )
}