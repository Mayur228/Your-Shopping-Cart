package com.demo.yourshoppingcart.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.demo.yourshoppingcart.common.ErrorView
import com.demo.yourshoppingcart.common.LoadingView
import com.demo.yourshoppingcart.ui.cart.CartState
import com.demo.yourshoppingcart.ui.cart.CartViewModel
import com.demo.yourshoppingcart.ui.home.component.CategoryList
import com.demo.yourshoppingcart.ui.home.component.HomeHeader
import com.demo.yourshoppingcart.ui.home.component.ItemList
import com.demo.yourshoppingcart.ui.wish_list.WishListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    isDark: Boolean,
    onThemeToggle: (isDark: Boolean) -> Unit,
    onCartClick: () -> Unit,
    onItemClick: (itemId: String) -> Unit,
    cartViewModel: CartViewModel,
    wishListViewModel: WishListViewModel
) {
    val homeViewModel = hiltViewModel<HomeViewModel>()
    val view by homeViewModel.viewState.collectAsState()
    val cartState by cartViewModel.viewState.collectAsState()

    var isDarkMode by remember { mutableStateOf(isDark) }

    Scaffold(
        topBar = {
            HomeHeader(
                title = "Home",
                isDarkMode = isDarkMode,
                cartCount = (cartState as? CartState.Success)?.cartEntity?.cartItem?.size ?: 0,
                onThemeToggle = {
                    isDarkMode = it
                    onThemeToggle(it)
                },
                onCartClick = onCartClick,
                onSearchClick = { }
            )
        }
    ) { paddingValues ->

        when (view) {

            is HomeViewState.Error -> ErrorView((view as HomeViewState.Error).message)

            HomeViewState.Loading -> LoadingView()

            is HomeViewState.Success -> {
                val product = (view as HomeViewState.Success)

                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(8.dp)
                        .fillMaxSize()
                ) {

                    Spacer(modifier = Modifier.height(8.dp))

                    CategoryList(
                        categories = product.categories,
                        onCategorySelected = { cat ->
                            if (cat == "All") homeViewModel.loadData()
                            else homeViewModel.getSelectedCatItem(cat)
                        },
                        isDark = isDarkMode
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    if (product.isLoading) {
                        LoadingView()
                    } else {
                        ItemList(
                            items = product.items,
                            onItemSelected = { onItemClick(it) },
                            cartViewModel = cartViewModel,
                            isDark = isDarkMode,
                            wishListViewModel = wishListViewModel
                        )
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        homeViewModel.viewEvent.collect { event ->
            when (event) {
                HomeEvent.LoadHomeData -> homeViewModel.loadData()
                HomeEvent.NavigateToCart -> onCartClick()
                is HomeEvent.NavigateToProductDetails -> onItemClick(event.productId)
                is HomeEvent.SelectCategory -> homeViewModel.getSelectedCatItem(event.categoryId)
                is HomeEvent.ToggleChange -> onThemeToggle(event.isDark)
            }
        }
    }
}
