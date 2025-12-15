package com.demo.yourshoppingcart.ui.product_details

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
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
import com.demo.yourshoppingcart.product_details.domain.entity.detailsEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsScreen(
    itemId: String,
    onBackClick: () -> Boolean,
    cartViewModel: CartViewModel,
) {
    val viewModel = hiltViewModel<ProductDetailsViewModel>()
    val viewState by viewModel.viewState.collectAsState()

    LaunchedEffect(Unit) { viewModel.getItemDetails(itemId) }

    when (viewState) {

        is ProductDetailsState.Error -> ErrorView((viewState as ProductDetailsState.Error).error)
        ProductDetailsState.Loading -> LoadingView()

        is ProductDetailsState.Success -> {
            val product = (viewState as ProductDetailsState.Success).product

            val pagerState = rememberPagerState { product.itemImages.size }
            val scrollState = rememberScrollState()

            var isWishlisted by remember { mutableStateOf(false) }

            val selectedQty =
                (cartViewModel.viewState.value as? CartState.Success)
                    ?.cartEntity?.cartItem
                    ?.find { it.productId == product.itemId }
                    ?.productQun ?: 0

            Scaffold(
                topBar = {},
                bottomBar = {
                    BottomCartBar(
                        qty = selectedQty,
                        product = product,
                        cartViewModel = cartViewModel
                    )
                }
            ) { paddingValues ->

                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .verticalScroll(scrollState)
                        .fillMaxWidth()
                ) {

                    /** IMAGE CAROUSEL */
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(360.dp)
                    ) {

                        HorizontalPager(state = pagerState) { page ->

                            Box(modifier = Modifier.fillMaxSize()) {

                                AsyncImage(
                                    model = product.itemImages[page],
                                    contentScale = ContentScale.Crop,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(
                                            RoundedCornerShape(
                                                bottomStart = 24.dp,
                                                bottomEnd = 24.dp
                                            )
                                        )
                                )

                                Box(
                                    modifier = Modifier
                                        .align(Alignment.BottomCenter)
                                        .fillMaxWidth()
                                        .height(120.dp)
                                        .background(
                                            Brush.verticalGradient(
                                                colors = listOf(
                                                    Color.Transparent,
                                                    MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                                                )
                                            )
                                        )
                                )
                            }
                        }

                        IconButton(
                            onClick = { onBackClick() },
                            modifier = Modifier
                                .padding(12.dp)
                                .size(40.dp)
                                .shadow(6.dp, CircleShape)
                                .background(MaterialTheme.colorScheme.surface)
                                .clip(CircleShape)
                                .align(Alignment.TopStart)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                        }

                        IconButton(
                            onClick = { isWishlisted = !isWishlisted },
                            modifier = Modifier
                                .padding(12.dp)
                                .size(40.dp)
                                .shadow(6.dp, CircleShape)
                                .background(MaterialTheme.colorScheme.surface)
                                .clip(CircleShape)
                                .align(Alignment.TopEnd)
                        ) {
                            Icon(
                                if (isWishlisted) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                contentDescription = "Wishlist",
                                tint = if (isWishlisted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                        }

                        /** DOTS */
                        Row(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            repeat(product.itemImages.size) { index ->
                                val selected = pagerState.currentPage == index
                                Box(
                                    modifier = Modifier
                                        .padding(3.dp)
                                        .size(if (selected) 10.dp else 6.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (selected) MaterialTheme.colorScheme.primary
                                            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                                        )
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(18.dp))

                    /** PRODUCT TITLE + PRICE + RATING */
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {

                            Text(
                                product.itemName,
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                            )

                            Spacer(Modifier.height(6.dp))

                            RatingRow(
                                rating = 4.5,
                                totalReviews = 120
                            )

                            Spacer(Modifier.height(8.dp))

                            Text(
                                "₹${product.itemPrice}",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            )

                            Spacer(Modifier.height(10.dp))

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50))
                                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f))
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text("In Stock", color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    /** DESCRIPTION */
                    Text(
                        "Description",
                        modifier = Modifier.padding(horizontal = 16.dp),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )

                    Text(
                        product.itemDescription,
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
fun RatingRow(rating: Double, totalReviews: Int) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        repeat(5) { index ->
            val filled = index < rating.toInt()
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                tint = if (filled) MaterialTheme.colorScheme.primary else Color.Gray,
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(Modifier.width(8.dp))
        Text("$rating | $totalReviews Reviews", color = Color.Gray)
    }
}

@Composable
fun BottomCartBar(
    qty: Int,
    product: detailsEntity,
    cartViewModel: CartViewModel
) {

    val transition = updateTransition(targetState = qty > 0, label = "CartTransition")

    val scaleAnim by transition.animateFloat(
        transitionSpec = { tween(350) },
        label = "Scale"
    ) { expanded ->
        if (expanded) 1f else 0.9f
    }

    Surface(
        tonalElevation = 12.dp,
        shadowElevation = 20.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            AnimatedContent(
                targetState = qty > 0,
                transitionSpec = {
                    slideInVertically { it } + fadeIn() togetherWith
                            slideOutVertically { it } + fadeOut()
                },
                label = "CartSwitcher"
            ) { hasQty ->

                if (!hasQty) {

                    Button(
                        onClick = {
                            cartViewModel.createOrUpdateCart(
                                product.itemId, 1,
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
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(58.dp)
                            .graphicsLayer {
                                scaleX = scaleAnim
                                scaleY = scaleAnim
                            },
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(
                            "Add to Cart",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }

                } else {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .clip(RoundedCornerShape(50))
                            .background(MaterialTheme.colorScheme.primary)
                            .graphicsLayer {
                                scaleX = scaleAnim
                                scaleY = scaleAnim
                            },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        IconButton(
                            onClick = {
                                cartViewModel.createOrUpdateCart(product.itemId, qty - 1)
                            }
                        ) {
                            Icon(
                                Icons.Default.Remove,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        Text(
                            "$qty",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onPrimary
                        )

                        IconButton(
                            onClick = {
                                cartViewModel.createOrUpdateCart(product.itemId, qty + 1)
                            }
                        ) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
