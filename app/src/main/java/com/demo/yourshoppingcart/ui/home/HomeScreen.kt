package com.demo.yourshoppingcart.ui.home

import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import com.demo.yourshoppingcart.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlin.getValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    isDarkTheme: Boolean,
    onThemeToggle: (isDark: Boolean) -> Unit,
    onCartClick: () -> Unit,
) {
    val homeViewModel = hiltViewModel<HomeViewModel>()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your Shopping Cart") },
                actions = {
                    // Theme switch button
                    IconButton(onClick = { onThemeToggle(isDarkTheme) }) {
                        Icon(
                            painter = painterResource(
                                id = if (isDarkTheme) R.drawable.dark_icon else R.drawable.light_icon
                            ),
                            contentDescription = "Toggle Theme"
                        )
                    }

                    // Cart button
                    IconButton(onClick = onCartClick) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Cart"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // Categories Section
            CategoryList(
                categories = listOf(
                    Category("Electronics", "https://via.placeholder.com/150"),
                    Category("Clothes", "https://via.placeholder.com/150"),
                    Category("Shoes", "https://via.placeholder.com/150"),
                    Category("Books", "https://via.placeholder.com/150")
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Item List Section
            ItemList(
                items = listOf(
                    ShopItem("Smartphone", "Latest 5G phone", 699.99, "https://via.placeholder.com/150"),
                    ShopItem("Sneakers", "Comfortable running shoes", 59.99, "https://via.placeholder.com/150"),
                    ShopItem("T-shirt", "Cotton casual wear", 19.99, "https://via.placeholder.com/150")
                )
            )
        }
    }

    LaunchedEffect(Unit) {
        homeViewModel.getAllCategory()
    }
}

// Category model
data class Category(val name: String, val icon: String?)

// Item model
data class ShopItem(val name: String, val description: String, val price: Double, val imageUrl: String)

@Composable
fun CategoryList(categories: List<Category>) {
    LazyRow(
        modifier = Modifier.padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(categories) { category ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(12.dp)
            ) {
                // Load icon from URL
                AsyncImage(
                    model = category.icon, // URL string
                    contentDescription = category.name,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop,
                )

                Spacer(modifier = Modifier.height(4.dp))
                Text(text = category.name, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}


@Composable
fun ItemList(items: List<ShopItem>) {
    LazyColumn(
        modifier = Modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items) { item ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Item Image
                    AsyncImage(
                        model = item.imageUrl,
                        contentDescription = item.name,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    // Item Info
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = item.name, style = MaterialTheme.typography.titleMedium)
                        Text(
                            text = item.description,
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "$${item.price}",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    // Add to Cart Button
                    Button(onClick = { /* TODO: Handle Add */ }) {
                        Text("Add")
                    }
                }
            }
        }
    }
}