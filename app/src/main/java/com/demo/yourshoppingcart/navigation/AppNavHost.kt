package com.demo.yourshoppingcart.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.demo.yourshoppingcart.R
import com.demo.yourshoppingcart.ui.cart.CartScreen
import com.demo.yourshoppingcart.ui.cart.CartViewModel
import com.demo.yourshoppingcart.ui.cart.CartState
import com.demo.yourshoppingcart.ui.checkout.CheckoutScreen
import com.demo.yourshoppingcart.ui.coupon.CouponsScreen
import com.demo.yourshoppingcart.ui.coupon.CouponsViewModel
import com.demo.yourshoppingcart.ui.home.HomeScreen
import com.demo.yourshoppingcart.ui.login.PhoneLoginScreen
import com.demo.yourshoppingcart.ui.orders.OrdersScreen
import com.demo.yourshoppingcart.ui.product_details.ProductDetailsScreen
import com.demo.yourshoppingcart.ui.profile.ProfileScreen
import com.demo.yourshoppingcart.ui.profile.ProfileViewModel
import com.demo.yourshoppingcart.ui.wish_list.WishListScreen
import com.demo.yourshoppingcart.ui.wish_list.WishListViewModel
import com.demo.yourshoppingcart.user.data.model.UserModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavHost(
    navController: NavHostController,
    isDarkTheme: Boolean,
    onThemeToggle: (Boolean) -> Unit,
) {
    val cartViewModel: CartViewModel = hiltViewModel()
    val couponsViewModel: CouponsViewModel = hiltViewModel()
    val profileViewModel: ProfileViewModel = hiltViewModel()
    val wishListViewModel: WishListViewModel = hiltViewModel()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            when (currentRoute) {
                NavRoutes.Home.route,
                NavRoutes.Cart.route,
                NavRoutes.Orders.route,
                NavRoutes.Profile.route,
                NavRoutes.WishList.route,
                NavRoutes.Coupons.route -> {
                    val cartState by cartViewModel.viewState.collectAsState()
                    val cartItemCount =
                        (cartState as? CartState.Success)?.cartEntity?.cartItem?.sumOf { it.productQun }
                            ?: 0

                    NavigationBar {
                        bottomNavItems.forEach { item ->
                            NavigationBarItem(
                                selected = currentRoute == item.route.route,
                                onClick = {
                                    navController.navigate(item.route.route) {
                                        launchSingleTop = true
                                    }
                                },
                                icon = {
                                    if (item.route.route == NavRoutes.Cart.route && cartItemCount > 0) {
                                        BadgedBox(badge = { Badge { Text(cartItemCount.toString()) } }) {
                                            Icon(item.icon, item.label)
                                        }
                                    } else {
                                        Icon(item.icon, item.label)
                                    }
                                },
                                label = { Text(item.label) }
                            )
                        }
                    }
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            NavHost(navController = navController, startDestination = NavRoutes.Home.route) {

                composable(NavRoutes.Home.route) {
                    HomeScreen(
                        onThemeToggle = onThemeToggle,
                        onCartClick = { navController.navigate(NavRoutes.Cart.route) },
                        onItemClick = { id ->
                            navController.navigate(
                                NavRoutes.ProductDetails.route(
                                    id
                                )
                            )
                        },
                        cartViewModel = cartViewModel,
                        isDark = isDarkTheme,
                    )
                }

                composable(NavRoutes.Cart.route) {
                    CartScreen(
                        onBackClick = { navController.popBackStack() },
                        cartViewModel = cartViewModel,
                        couponViewModel = couponsViewModel,
                        isPhoneLogin = true,
                        navigateToPhoneLogin = { navController.navigate(NavRoutes.PhoneLogin.route) },
                        onPlaceOrder = { navController.navigate(NavRoutes.Checkout.route) },
                        onApplyCoupon = { navController.navigate(NavRoutes.Coupons.route) }
                    )
                }

                composable(NavRoutes.Coupons.route) {
                    CouponsScreen(
                        viewModel = couponsViewModel,
                        onBack = { navController.popBackStack() }
                    )
                }

                composable(NavRoutes.ProductDetails.route) { entry ->
                    val id = entry.arguments?.getString("id") ?: ""
                    ProductDetailsScreen(
                        itemId = id,
                        onBackClick = { navController.popBackStack() },
                        cartViewModel = cartViewModel,
                        wishListViewModel = wishListViewModel
                    )
                }

                composable(NavRoutes.Checkout.route) {
                    CheckoutScreen(
                        onBackClick = { navController.popBackStack() },
                        cartViewModel = cartViewModel,
                        couponViewModel = couponsViewModel,
                        onPlaceOrder = { cartViewModel.checkout() },
                        onCoupons = { navController.navigate(NavRoutes.Coupons.route) }
                    )
                }

                composable(NavRoutes.PhoneLogin.route) {
                    PhoneLoginScreen(onLoginSuccess = { navController.popBackStack() })
                }

                composable(NavRoutes.Orders.route) {
                    OrdersScreen()
                }

                composable(NavRoutes.Profile.route) {
                    ProfileScreen(
                        profileViewModel = profileViewModel,
                        onLogout = {}
                    )
                }

                composable(NavRoutes.WishList.route) {
                    WishListScreen(
                        onProductClick = {
                            navController.navigate(NavRoutes.ProductDetails.route(id = it))
                        },
                        viewModel = wishListViewModel
                    )
                }
            }
        }
    }
}