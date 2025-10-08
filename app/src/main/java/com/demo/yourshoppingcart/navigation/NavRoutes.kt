package com.demo.yourshoppingcart.navigation

object NavRoutes {
    const val HOME = "home"
    const val PRODUCT_DETAILS = "product_details"

    fun productDetails(itemId: String) = "$PRODUCT_DETAILS/$itemId"
}
