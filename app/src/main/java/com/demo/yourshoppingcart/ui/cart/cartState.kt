package com.demo.yourshoppingcart.ui.cart

import com.demo.yourshoppingcart.cart.domain.entity.cartEntity

sealed class CartState{
    object Loading: CartState()
    data class Success(val cartEntity: cartEntity): CartState()
    data class Error(val error: String): CartState()
}
