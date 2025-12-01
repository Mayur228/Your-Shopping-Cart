package com.demo.yourshoppingcart.navigation

sealed class NavRoutes(
    val route: String,
    val title: String? = null,
) {
    data object Home : NavRoutes("home", "Home")
    data object Cart : NavRoutes("cart", "My Cart")
    data object Coupons : NavRoutes("coupons", "Coupons")
    data object Orders : NavRoutes("orders", "My Orders")
    data object Profile : NavRoutes("profile", "Profile")

    data object Checkout : NavRoutes("checkout", "CheckOut")
    data object PhoneLogin : NavRoutes("phone_login", "Phone Login")

    data object ProductDetails : NavRoutes("product_details/{id}", null) {
        fun route(id: String) = "product_details/$id"
    }
}
