package com.demo.yourshoppingcart.ui.cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.demo.yourshoppingcart.common.QuantityView
import com.demo.yourshoppingcart.common.QuantityViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    productIds: List<String>?,
    cartId: String,
    onBackClick: () -> Unit,
    quantityViewModel: QuantityViewModel,
    cartViewModel: CartViewModel
) {
    val viewState by cartViewModel.viewState.collectAsState()

    // Load cart once
    LaunchedEffect(cartId, productIds) {
        cartViewModel.loadCart(
            productIds = productIds,
            cartId = cartId,
            productQuantity = quantityViewModel.quantities
        )
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
            val totalPrice = viewState.cartData?.cartItem?.sumOf {
                it.productQun * (it.productPrice.toIntOrNull() ?: 0)
            } ?: 0

            if (totalPrice > 0) {
                Button(
                    onClick = {
                        cartViewModel.checkout(cartId)
                        quantityViewModel.reset()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("Checkout (₹$totalPrice)")
                }
            }
        }
    ) { innerPadding ->

        when {
            viewState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            viewState.cartData?.cartItem.isNullOrEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Your cart is empty")
                }
            }

            else -> {
                val cartItems = viewState.cartData?.cartItem ?: emptyList()

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(cartItems) { item ->
                        QuantityView(productId = item.productId, viewModel = quantityViewModel) {
                                quantity, onIncrease, onDecrease ->
                            ProductItemCard(
                                itemName = item.productName,
                                itemPrice = item.productPrice,
                                itemDes = item.productDes,
                                itemImg = item.productImg,
                                quantity = quantity,
                                onIncrease = {
                                    onIncrease()
                                    cartViewModel.updateCartQuantity(
                                        cartId,
                                        item.productId,
                                        quantity + 1
                                    )
                                },
                                onDecrease = {
                                    onDecrease()
                                    cartViewModel.updateCartQuantity(
                                        cartId,
                                        item.productId,
                                        quantity - 1
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun ProductItemCard(
    itemName: String,
    itemPrice: String,
    itemDes: String,
    itemImg: String,
    quantity: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = rememberAsyncImagePainter(itemImg),
                contentDescription = itemName,
                modifier = Modifier
                    .size(72.dp)
                    .background(Color.White, RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(itemName, style = MaterialTheme.typography.titleMedium)
                Text("₹$itemPrice", style = MaterialTheme.typography.bodyLarge)
                Text(
                    itemDes,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    color = Color.Gray
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onDecrease) {
                    Icon(Icons.Default.Remove, contentDescription = "Decrease")
                }
                Text(quantity.toString(), style = MaterialTheme.typography.titleMedium)
                IconButton(onClick = onIncrease) {
                    Icon(Icons.Default.Add, contentDescription = "Increase")
                }
            }
        }
    }
}
