package com.demo.yourshoppingcart.navigation

import com.demo.yourshoppingcart.user.domain.entity.cartEntity

object NavRoutes {
    const val HOME = "home"
    const val PRODUCT_DETAILS = "product_details"
    const val CART = "cart"


    fun productDetails(itemId: String) = "$PRODUCT_DETAILS/$itemId"
}
