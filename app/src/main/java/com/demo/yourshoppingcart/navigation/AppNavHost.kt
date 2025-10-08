package com.demo.yourshoppingcart.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.demo.yourshoppingcart.ui.home.HomeScreen
import com.demo.yourshoppingcart.ui.product_details.ProductDetailsScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    isDarkTheme: Boolean,
    onThemeToggle: (Boolean) -> Unit,
    onCartClick: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.HOME
    ) {
        composable(NavRoutes.HOME) {
            HomeScreen(
                isDarkTheme = isDarkTheme,
                onThemeToggle = onThemeToggle,
                onCartClick = onCartClick,
                onItemClick = { itemId ->
                    navController.navigate(NavRoutes.productDetails(itemId))
                }
            )
        }

        composable(
            route = "${NavRoutes.PRODUCT_DETAILS}/{itemId}",
            arguments = listOf(navArgument("itemId") { type = NavType.StringType })
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getString("itemId") ?: ""
            ProductDetailsScreen(
                itemId = itemId,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
