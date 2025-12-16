package com.demo.yourshoppingcart.ui.home.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.demo.yourshoppingcart.cart.domain.entity.cartItemEntity
import com.demo.yourshoppingcart.home.domain.entity.productEntity
import com.demo.yourshoppingcart.ui.cart.CartState
import com.demo.yourshoppingcart.ui.cart.CartViewModel
import com.demo.yourshoppingcart.ui.wish_list.WishListState
import com.demo.yourshoppingcart.ui.wish_list.WishListViewModel
import com.demo.yourshoppingcart.wish_list.domain.entity.wishListEntity
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Composable
fun ItemList(
    items: List<productEntity>,
    onItemSelected: (itemId: String) -> Unit,
    cartViewModel: CartViewModel,
    isDark: Boolean,
    wishListViewModel: WishListViewModel
) {

    val cartState by cartViewModel.viewState.collectAsState()
    val cartItems = (cartState as? CartState.Success)?.cartEntity?.cartItem ?: emptyList()
    val wishListState by wishListViewModel.state.collectAsState()

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(10.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {

        items(items) { item ->

            val quantity = cartItems.find { it.productId == item.productId }?.productQun ?: 0

            val imgHeight = remember(item.productId) {
                arrayOf(165.dp, 185.dp, 205.dp, 225.dp).random()
            }

            val lift by animateFloatAsState(
                targetValue = if (quantity > 0) 1.02f else 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )

            Box(
                modifier = Modifier
                    .graphicsLayer {
                        scaleX = lift
                        scaleY = lift
                        shadowElevation = if (isDark) 22f else 12f
                        shape = RoundedCornerShape(22.dp)
                        clip = false
                    }
                    .then(
                        if (isDark)
                            Modifier
                                .shadow(
                                    elevation = 22.dp,
                                    shape = RoundedCornerShape(22.dp),
                                    ambientColor = Color(0x33000000),
                                    spotColor = Color(0x66000000)
                                )
                                .background(
                                    Color(0x44202024), // dark glass effect
                                    RoundedCornerShape(22.dp)
                                )
                        else Modifier.shadow(
                            elevation = 10.dp,
                            shape = RoundedCornerShape(22.dp),
                            ambientColor = Color.Black.copy(alpha = 0.08f),
                            spotColor = Color.Black.copy(alpha = 0.12f)
                        )
                    )
                    .border(
                        width = 1.dp,
                        brush = if (isDark)
                            Brush.linearGradient(
                                listOf(
                                    Color(0x33FFFFFF),
                                    Color(0x11FFFFFF)
                                )
                            )
                        else Brush.linearGradient(
                            listOf(
                                Color.Black.copy(alpha = 0.10f),
                                Color.Transparent
                            )
                        ),
                        shape = RoundedCornerShape(22.dp)
                    )
            ) {

                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(22.dp))
                        .clickable { onItemSelected(item.productId) },
                    elevation = CardDefaults.cardElevation(0.dp),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(
                        containerColor =
                            if (isDark)
                                Color(0xFF1C1C20)
                            else Color.White
                    )
                ) {

                    Column(modifier = Modifier.padding(12.dp)) {

                        Box {

                            AsyncImage(
                                model = item.productImg,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(imgHeight)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(
                                                MaterialTheme.colorScheme.primary.copy(alpha = 0.20f),
                                                Color.Transparent
                                            )
                                        )
                                    ),
                                contentScale = ContentScale.Crop
                            )

                            // Discount tag
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopStart)
                                    .padding(8.dp)
                                    .background(
                                        MaterialTheme.colorScheme.primary,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "-${item.productDiscount}%",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }

                            // Wishlist button
                            val isWishListed = remember(wishListState, item.productId) {
                                (wishListState as? WishListState.Success)
                                    ?.list
                                    ?.any { it.product?.productId == item.productId }
                                    ?: false
                            }
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(8.dp)
                                    .size(38.dp)
                                    .background(
                                        if (isDark) Color.Black.copy(alpha = 0.45f)
                                        else Color.White.copy(alpha = 0.75f),
                                        CircleShape
                                    )
                                    .clickable {
                                        if (isWishListed) {
                                            wishListViewModel.removeWishList(item.productId)
                                        } else {
                                            wishListViewModel.addToWishList(
                                                wishList = wishListEntity(
                                                    id = Uuid.random().toString(),
                                                    product = null //need to fix this
                                                )
                                            )
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (isWishListed) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                    contentDescription = null,
                                    tint = if (isWishListed)
                                        MaterialTheme.colorScheme.error
                                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                                )
                            }
                        }

                        Spacer(Modifier.height(10.dp))

                        // Title
                        Text(
                            item.productName,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )

                        // Description
                        Text(
                            item.productDes,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodySmall
                        )

                        Spacer(Modifier.height(8.dp))

                        // Rating + Price
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                repeat(5) { i ->
                                    Icon(
                                        Icons.Default.Star,
                                        contentDescription = "rating",
                                        tint = if (i < item.productRating.toInt())
                                            MaterialTheme.colorScheme.secondary
                                        else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }

                            Text(
                                "$${item.productPrice}",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }

                        Spacer(Modifier.height(14.dp))

                        AnimatedContent(
                            targetState = quantity,
                            transitionSpec = {
                                fadeIn(tween(200)) + slideInVertically(tween(200)) togetherWith
                                        fadeOut(tween(150))
                            }
                        ) { qty ->

                            if (qty == 0) {
                                Button(
                                    onClick = {
                                        cartViewModel.createOrUpdateCart(
                                            item.productId,
                                            1,
                                            cartItemEntity(
                                                productId = item.productId,
                                                productName = item.productName,
                                                productPrice = item.productPrice.toString(),
                                                productQun = 1,
                                                productDes = item.productDes,
                                                productImg = item.productImg
                                            )
                                        )
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(50),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary,
                                        contentColor = MaterialTheme.colorScheme.onPrimary
                                    )
                                ) {
                                    Icon(Icons.Default.Add, null)
                                    Spacer(Modifier.width(6.dp))
                                    Text("Add")
                                }

                            } else {

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(50))
                                        .background(
                                            if (isDark)
                                                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f)
                                            else
                                                MaterialTheme.colorScheme.surfaceVariant
                                        )
                                        .padding(horizontal = 12.dp, vertical = 6.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    IconButton(onClick = {
                                        cartViewModel.createOrUpdateCart(item.productId, qty - 1)
                                    }) {
                                        Icon(Icons.Default.Remove, null)
                                    }

                                    Text(
                                        "$qty",
                                        style = MaterialTheme.typography.titleMedium
                                    )

                                    IconButton(onClick = {
                                        cartViewModel.createOrUpdateCart(item.productId, qty + 1)
                                    }) {
                                        Icon(Icons.Default.Add, null)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
