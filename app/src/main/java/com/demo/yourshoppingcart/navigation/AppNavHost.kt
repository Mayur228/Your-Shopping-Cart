package com.demo.yourshoppingcart.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.demo.yourshoppingcart.common.QuantityViewModel
import com.demo.yourshoppingcart.ui.cart.CartScreen
import com.demo.yourshoppingcart.ui.home.HomeScreen
import com.demo.yourshoppingcart.ui.product_details.ProductDetailsScreen
import com.demo.yourshoppingcart.user.data.model.UserModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    isDarkTheme: Boolean,
    onThemeToggle: (Boolean) -> Unit,
    user: UserModel.UserResponse
) {
    val quantityViewModel: QuantityViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = NavRoutes.HOME
    ) {

        composable(NavRoutes.HOME) {
            HomeScreen(
                isDarkTheme = isDarkTheme,
                onThemeToggle = onThemeToggle,
                onCartClick = {
                    navController.navigate(NavRoutes.cartScreen(it))
                },
                onItemClick = { itemId ->
                    navController.navigate(NavRoutes.productDetails(itemId))
                },
                quantityViewModel = quantityViewModel
            )
        }

        composable(
            route = "${NavRoutes.PRODUCT_DETAILS}/{itemId}",
            arguments = listOf(navArgument("itemId") { type = NavType.StringType })
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getString("itemId") ?: ""
            ProductDetailsScreen(
                itemId = itemId,
                onBackClick = { navController.popBackStack() },
                quantityViewModel = quantityViewModel
            )
        }

        composable(
            route = "${NavRoutes.CART}/{cartId}",
            arguments = listOf(navArgument("cartId") { type = NavType.StringType })

        ) {backStackEntry ->
            val cartId = backStackEntry.arguments?.getString("cartId") ?: ""
            Log.e("CHECK",user.cart?.cartId.toString())
            CartScreen(
                onBackClick = { navController.popBackStack() },
                productIds = quantityViewModel.quantities.keys.toList(),
                cartId = cartId,
                quantityViewModel = quantityViewModel
            )
        }
    }
}
