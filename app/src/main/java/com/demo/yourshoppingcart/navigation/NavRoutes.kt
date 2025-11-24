package com.demo.yourshoppingcart.navigation

import com.demo.yourshoppingcart.user.domain.entity.cartEntity

object NavRoutes {
    const val HOME = "home"
    const val PRODUCT_DETAILS = "product_details"
    const val CART = "cart"
    const val PHONE_LOGIN = "phone_login"
    const val CHECKOUT = "checkout"


    fun productDetails(itemId: String) = "$PRODUCT_DETAILS/$itemId"
    fun cartScreen(cartId: String?) = "$CART/$cartId"
}
