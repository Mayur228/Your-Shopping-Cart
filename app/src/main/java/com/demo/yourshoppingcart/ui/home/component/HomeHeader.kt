package com.demo.yourshoppingcart.ui.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun HomeHeader(
    title: String = "Home",
    isDarkMode: Boolean,
    cartCount: Int,
    onThemeToggle: (Boolean) -> Unit,
    onCartClick: () -> Unit,
    onSearchClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {

        // TOP ROW
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp),   // small padding only
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Row(verticalAlignment = Alignment.CenterVertically) {

                IconToggleButton(
                    checked = isDarkMode,
                    onCheckedChange = onThemeToggle
                ) {
                    Icon(
                        imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // CART BUTTON
                // CART BUTTON (NO MORE CLIPPING)
                Box(
                    modifier = Modifier
                        .size(46.dp)  // FIXED: Increased size so badge won't be clipped
                        .clickable { onCartClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Cart",
                        tint = MaterialTheme.colorScheme.primary
                    )

                    if (cartCount > 0) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .offset(x = 4.dp, y = (-4).dp) // FIXED: More visible offset
                                .size(20.dp)                  // FIXED: Slightly larger so text won't cut
                                .background(
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = CircleShape
                                )
                                .padding(1.dp),                // FIXED: Extra safe padding
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = cartCount.toString(),
                                color = MaterialTheme.colorScheme.onPrimary,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }

            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // SEARCH BAR (flat)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.medium)  // subtle, not card-like
                .background(
                    if (isDarkMode) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f)
                    else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f)
                )
                .clickable { onSearchClick() }
                .padding(horizontal = 14.dp, vertical = 12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.outline
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Search products...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}
