package com.demo.yourshoppingcart.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Discount
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val route: NavRoutes,
    val label: String,
    val icon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem(NavRoutes.Home, "Home", Icons.Default.Home),
    BottomNavItem(NavRoutes.Cart, "Cart", Icons.Default.ShoppingCart),
    BottomNavItem(NavRoutes.Orders, "Orders", Icons.AutoMirrored.Filled.List),
    BottomNavItem(NavRoutes.Coupons, "Coupons", Icons.Default.Discount),
    BottomNavItem(NavRoutes.Profile, "Profile", Icons.Default.Person),
    BottomNavItem(NavRoutes.WishList, "WishList", Icons.Default.Favorite)
)

