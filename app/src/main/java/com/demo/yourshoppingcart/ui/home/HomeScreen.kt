package com.demo.yourshoppingcart.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.demo.yourshoppingcart.R
import com.demo.yourshoppingcart.common.QuantityViewModel
import com.demo.yourshoppingcart.ui.home.component.CategoryList
import com.demo.yourshoppingcart.ui.home.component.ItemList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    isDarkTheme: Boolean,
    onThemeToggle: (isDark: Boolean) -> Unit,
    onCartClick: (userId: String?) -> Unit,
    onItemClick: (itemId: String) -> Unit,
    quantityViewModel: QuantityViewModel
) {
    val homeViewModel = hiltViewModel<HomeViewModel>()
    val view by homeViewModel.viewState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                actions = {
                    IconButton(onClick = {
                        onThemeToggle(!isDarkTheme)
                    }) {
                        Icon(
                            painter = painterResource(
                                id = if (isDarkTheme) R.drawable.dark_icon else R.drawable.light_icon
                            ),
                            contentDescription = "Toggle Theme"
                        )
                    }

                    IconButton(onClick = {
                        onCartClick(null)
                    }) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Cart"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        when {
            view.isLoading -> {
                //loading view
            }

            view.errorMessage?.isNotEmpty() == true -> {
                //error view
            }

            else -> {
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                ) {
                    // Categories View
                    CategoryList(
                        categories = view.categories,
                        onCategorySelected = {
                            homeViewModel.getSelectedCatItem(it)
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    //Items View
                    if (view.isItemLoading) {
                        //loading view
                    } else {
                        ItemList(
                            items = view.items,
                            onItemSelected = {
                                //Navigate to Details Screen
                                onItemClick(it)
                            },
                            quantityViewModel = quantityViewModel
                        )
                    }
                }
            }
        }
    }
}