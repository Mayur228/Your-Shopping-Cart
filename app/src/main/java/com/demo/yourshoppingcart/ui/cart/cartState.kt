package com.demo.yourshoppingcart.ui.cart

import com.demo.yourshoppingcart.user.domain.entity.cartEntity

data class CartState(
    val isLoading: Boolean = false,
    val cartData: cartEntity? = null,
    val errorMessage: String? = null
)