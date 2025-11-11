package com.demo.yourshoppingcart.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.demo.yourshoppingcart.ui.cart.CartScreen
import com.demo.yourshoppingcart.ui.cart.CartViewModel
import com.demo.yourshoppingcart.ui.home.HomeScreen
import com.demo.yourshoppingcart.ui.login.PhoneLoginScreen
import com.demo.yourshoppingcart.ui.product_details.ProductDetailsScreen
import com.demo.yourshoppingcart.user.data.model.USERTYPE
import com.demo.yourshoppingcart.user.data.model.UserModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    isDarkTheme: Boolean,
    onThemeToggle: (Boolean) -> Unit,
    user: UserModel.UserResponse?
) {
    val cartViewModel: CartViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = NavRoutes.HOME
    ) {

        composable(NavRoutes.HOME) {
            HomeScreen(
                isDarkTheme = isDarkTheme,
                onThemeToggle = onThemeToggle,
                onCartClick = {
                    navController.navigate(NavRoutes.CART)
                },
                onItemClick = { itemId ->
                    navController.navigate(NavRoutes.productDetails(itemId))
                },
                cartViewModel = cartViewModel,
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
                cartViewModel = cartViewModel,
            )
        }

        composable(
            route = NavRoutes.CART,
        ) {backStackEntry ->
            CartScreen(
                onBackClick = { navController.popBackStack() },
                cartViewModel = cartViewModel,
                isPhoneLogin = user?.userType == USERTYPE.LOGGED,
                navigateToPhoneLogin = {
                    navController.navigate(NavRoutes.PHONE_LOGIN)
                }
            )
        }

        composable(route = NavRoutes.PHONE_LOGIN) {
            PhoneLoginScreen(
                onLoginSuccess = {
                    navController.popBackStack()
                }
            )
        }
    }
}
