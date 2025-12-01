package com.demo.yourshoppingcart.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.demo.yourshoppingcart.common.ErrorView
import com.demo.yourshoppingcart.common.LoadingView
import com.demo.yourshoppingcart.ui.cart.CartViewModel
import com.demo.yourshoppingcart.ui.home.component.CategoryList
import com.demo.yourshoppingcart.ui.home.component.ItemList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onThemeToggle: (isDark: Boolean) -> Unit,
    onCartClick: () -> Unit,
    onItemClick: (itemId: String) -> Unit,
    cartViewModel: CartViewModel,
) {
    val homeViewModel = hiltViewModel<HomeViewModel>()
    val view by homeViewModel.viewState.collectAsState()

    when (view) {
        is HomeViewState.Error -> ErrorView((view as HomeViewState.Error).message)
        HomeViewState.Loading -> LoadingView()
        is HomeViewState.Success -> {
            val product = (view as HomeViewState.Success)
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxSize()
            ) {
                CategoryList(
                    categories = product.categories,
                    onCategorySelected = { homeViewModel.getSelectedCatItem(it) }
                )
                Spacer(modifier = Modifier.height(16.dp))
                if (product.isLoading) {
                    LoadingView()
                } else {
                    ItemList(
                        items = product.items,
                        onItemSelected = { onItemClick(it) },
                        cartViewModel = cartViewModel,
                    )
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