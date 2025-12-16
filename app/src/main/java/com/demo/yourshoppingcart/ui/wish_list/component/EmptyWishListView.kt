package com.demo.yourshoppingcart.ui.wish_list.component

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun EmptyWishListView() {

    // 🔁 Infinite heartbeat animation
    val infiniteTransition = rememberInfiniteTransition(label = "heartBeat")

    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 600,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scaleAnimation"
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                tint = Color.Red,
                modifier = Modifier
                    .size(80.dp)
                    .scale(scale) // ❤️ heartbeat here
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = "Your wishlist is empty",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "Save items you like ❤️",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}
