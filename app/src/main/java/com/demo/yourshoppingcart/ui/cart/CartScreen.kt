package com.demo.yourshoppingcart.ui.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    productIds: List<String>?,
    onBackClick: () -> Unit,
    cartId: String
) {
    val cartViewModel: CartViewModel = hiltViewModel()
    val viewState by cartViewModel.viewState.collectAsState()

    LaunchedEffect(Unit) {
        cartViewModel.loadCart(productIds, cartId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your Cart") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            viewState.cartData?.let { cart ->
                if (cart.catItem.isNotEmpty()) {
                    val totalPrice = cart.catItem.sumOf { it.itemQun * 100 }
                    Button(
                        onClick = { cartViewModel.checkout(cartId) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text("Checkout (₹$totalPrice)")
                    }
                }
            }
        }
    ) { innerPadding ->
        viewState.cartData?.let { cart ->
            if (cart.catItem.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Your cart is empty")
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(cart.catItem) { item ->
                        Text("${item.itemId} - Qty: ${item.itemQun}")
                    }
                }
            }
        }
    }
}
