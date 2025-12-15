package com.demo.yourshoppingcart.ui.home.component

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.demo.yourshoppingcart.cart.domain.entity.cartItemEntity
import com.demo.yourshoppingcart.home.domain.entity.HomeEntity
import com.demo.yourshoppingcart.ui.cart.CartState
import com.demo.yourshoppingcart.ui.cart.CartViewModel

@Composable
fun ItemList(
    items: List<HomeEntity.ItemEntity>,
    onItemSelected: (itemId: String) -> Unit,
    cartViewModel: CartViewModel,
    isDark: Boolean
) {

    val cartState by cartViewModel.viewState.collectAsState()
    val cartItems = (cartState as? CartState.Success)?.cartEntity?.cartItem ?: emptyList()

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(10.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {

        items(items) { item ->

            val quantity = cartItems.find { it.productId == item.id }?.productQun ?: 0

            // Staggered card height
            val imgHeight = remember(item.id) {
                arrayOf(165.dp, 185.dp, 205.dp, 225.dp).random()
            }

            val rating = remember(item.id) { 3.5 + (item.id.hashCode() % 20) / 10f }
            val discount = remember(item.id) { (5..40).random() }

            // Lift animation when quantity > 0
            val lift by animateFloatAsState(
                targetValue = if (quantity > 0) 1.02f else 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )

            // ────────────────────────────────────────────────────────────────
            // ULTRA FUSION CARD WRAPPER (shadow + border + glass)
            // ────────────────────────────────────────────────────────────────
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
                            Modifier.shadow(
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

                // ────────────────────────────────────────────────────────────────
                // MAIN CARD LAYER (glass + surface)
                // ────────────────────────────────────────────────────────────────
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(22.dp))
                        .clickable { onItemSelected(item.id) },
                    elevation = CardDefaults.cardElevation(0.dp),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(
                        containerColor =
                            if (isDark)
                                Color(0xFF1C1C20) // PERFECT dark card color
                            else Color.White
                    )
                ) {

                    Column(modifier = Modifier.padding(12.dp)) {

                        // ────────────────────────────────────────────────────────────────
                        // IMAGE BLOCK
                        // ────────────────────────────────────────────────────────────────
                        Box {

                            AsyncImage(
                                model = item.img,
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
                                    text = "-$discount%",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }

                            // Wishlist button
                            var fav by remember { mutableStateOf(false) }

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
                                    .clickable { fav = !fav },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (fav) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                    contentDescription = null,
                                    tint = if (fav)
                                        MaterialTheme.colorScheme.error
                                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                                )
                            }
                        }

                        Spacer(Modifier.height(10.dp))

                        // Title
                        Text(
                            item.name,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )

                        // Description
                        Text(
                            item.des,
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
                                        tint = if (i < rating.toInt())
                                            MaterialTheme.colorScheme.secondary
                                        else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }

                            Text(
                                "$${item.price}",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }

                        Spacer(Modifier.height(14.dp))

                        // ────────────────────────────────────────────────────────────────
                        // ADD / QUANTITY BLOCK
                        // ────────────────────────────────────────────────────────────────
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
                                            item.id,
                                            1,
                                            cartItemEntity(
                                                productId = item.id,
                                                productName = item.name,
                                                productPrice = item.price,
                                                productQun = 1,
                                                productDes = item.des,
                                                productImg = item.img
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
                                        cartViewModel.createOrUpdateCart(item.id, qty - 1)
                                    }) {
                                        Icon(Icons.Default.Remove, null)
                                    }

                                    Text(
                                        "$qty",
                                        style = MaterialTheme.typography.titleMedium
                                    )

                                    IconButton(onClick = {
                                        cartViewModel.createOrUpdateCart(item.id, qty + 1)
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
