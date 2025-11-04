package com.demo.yourshoppingcart.ui.cart

import com.demo.yourshoppingcart.cart.domain.entity.cartEntity

data class CartState(
    val isLoading: Boolean = false,
    val cartData: cartEntity? = null,
    val errorMessage: String? = null
)
/*
sealed class CartViewState {
    object Loading
    data class CartData(
        val cartId: String,
        val cart: cartEntity
    )
}*/
