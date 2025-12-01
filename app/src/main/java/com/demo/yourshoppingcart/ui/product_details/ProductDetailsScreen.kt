package com.demo.yourshoppingcart.ui.product_details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import com.demo.yourshoppingcart.cart.domain.entity.cartItemEntity
import com.demo.yourshoppingcart.ui.cart.CartState
import com.demo.yourshoppingcart.ui.cart.CartViewModel
import com.demo.yourshoppingcart.common.ErrorView
import com.demo.yourshoppingcart.common.LoadingView

@Composable
fun ProductDetailsScreen(
    itemId: String,
    onBackClick: () -> Boolean,
    cartViewModel: CartViewModel,
) {
    val viewModel = hiltViewModel<ProductDetailsViewModel>()
    val view by viewModel.viewState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getItemDetails(itemId)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when (view) {
            is ProductDetailsState.Error -> ErrorView((view as ProductDetailsState.Error).error)
            ProductDetailsState.Loading -> LoadingView()
            is ProductDetailsState.Success -> {
                val product = (view as ProductDetailsState.Success).product
                val pagerState = rememberPagerState(pageCount = { product.itemImages.size })
                val quantity =
                    (cartViewModel.viewState.value as? CartState.Success)
                        ?.cartEntity?.cartItem
                        ?.find { product.itemId == it.productId }
                        ?.productQun ?: 0

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(bottom = 80.dp) // Reserve space for bottom button
                ) {
                    // Carousel
                    Box(modifier = Modifier.fillMaxWidth()) {
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                        ) { page ->
                            AsyncImage(
                                model = product.itemImages[page],
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }

                        // Dots indicator
                        Row(
                            Modifier
                                .align(Alignment.BottomCenter)
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            repeat(product.itemImages.size) { index ->
                                val isSelected = pagerState.currentPage == index
                                Box(
                                    modifier = Modifier
                                        .padding(2.dp)
                                        .size(if (isSelected) 8.dp else 6.dp)
                                        .background(
                                            color = if (isSelected) MaterialTheme.colorScheme.primary
                                            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                                            shape = CircleShape
                                        )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = product.itemName,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "₹${product.itemPrice}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        ),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = product.itemDescription,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }

                // Bottom Add/Update Button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                ) {
                    if (quantity == 0) {
                        Button(
                            onClick = {
                                cartViewModel.createOrUpdateCart(
                                    product.itemId,
                                    1,
                                    cartItem = cartItemEntity(
                                        productId = product.itemId,
                                        productName = product.itemName,
                                        productPrice = product.itemPrice,
                                        productQun = 1,
                                        productDes = product.itemDescription,
                                        productImg = product.itemImage
                                    )
                                )
                            },
                            shape = RoundedCornerShape(50),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Add to Cart", style = MaterialTheme.typography.bodyLarge)
                        }
                    } else {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .clip(RoundedCornerShape(50))
                                .background(MaterialTheme.colorScheme.primary)
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            IconButton(onClick = {
                                cartViewModel.createOrUpdateCart(product.itemId, quantity - 1)
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Remove,
                                    contentDescription = "Decrease",
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }

                            Text(
                                text = quantity.toString(),
                                style = MaterialTheme.typography.titleMedium,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )

                            IconButton(onClick = {
                                cartViewModel.createOrUpdateCart(product.itemId, quantity + 1)
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Increase",
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}