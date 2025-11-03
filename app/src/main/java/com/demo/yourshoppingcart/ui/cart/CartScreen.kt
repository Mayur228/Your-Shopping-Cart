package com.demo.yourshoppingcart.ui.cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.demo.yourshoppingcart.common.QuantityViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    productIds: List<String>?,
    cartId: String,
    onBackClick: () -> Unit,
    quantityViewModel: QuantityViewModel
) {
    val cartViewModel: CartViewModel = hiltViewModel()
    val viewState by cartViewModel.viewState.collectAsState()

    LaunchedEffect(cartId) {
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
            val totalPrice = viewState.cartData?.cartItem?.sumOf { it.productQun * (it.productPrice.toIntOrNull() ?: 0) } ?: 0
            if (totalPrice > 0) {
                Button(
                    onClick = {
                        cartViewModel.checkout(cartId)
                        quantityViewModel.reset()
                    },
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                ) {
                    Text("Checkout (₹$totalPrice)")
                }
            }
        }
    ) { innerPadding ->

        if (viewState.isLoading) {
            //Loading View
        }else if (viewState.cartData != null) {
            val cartItems = viewState.cartData?.cartItem ?: emptyList()
            if (cartItems.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize().padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Your cart is empty")
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(cartItems) { item ->
                        ProductItemCard(
                            itemId = item.productId,
                            itemName = item.productName,
                            itemPrice = item.productPrice,
                            itemDes = item.productDes,
                            itemImg = item.productImg,
                            quantity = item.productQun,
                            onIncrease = {
                                quantityViewModel.increaseQuantity(item.productId)
                                //cartViewModel.updateCart(cartId,cartItems)
                            },
                            onDecrease = {
                                quantityViewModel.decreaseQuantity(item.productId)
                                //cartViewModel.updateCart(cartId,cartItems)
                            }
                        )
                    }
                }
            }
        }else {
            Text(viewState.errorMessage ?: "", color = Color.White)
        }
    }
}

@Composable
fun ProductItemCard(
    itemId: String,
    itemName: String,
    itemPrice: String,
    itemDes: String,
    itemImg: String,
    quantity: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Product Image
            Image(
                painter = rememberAsyncImagePainter(itemImg),
                contentDescription = itemName,
                modifier = Modifier
                    .size(64.dp)
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Product Info
            Column(modifier = Modifier.weight(1f)) {
                Text(itemName, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text("₹$itemPrice", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(2.dp))
                Text(itemDes, style = MaterialTheme.typography.bodySmall, maxLines = 2)
            }

            // Quantity Controls
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onDecrease) {
                    Text("-")
                }
                Text(quantity.toString(), modifier = Modifier.padding(horizontal = 8.dp))
                IconButton(onClick = onIncrease) {
                    Text("+")
                }
            }
        }
    }
}
