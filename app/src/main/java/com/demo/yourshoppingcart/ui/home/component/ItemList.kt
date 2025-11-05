package com.demo.yourshoppingcart.ui.home.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.demo.yourshoppingcart.common.QuantityView
import com.demo.yourshoppingcart.common.QuantityViewModel
import com.demo.yourshoppingcart.home.domain.entity.HomeEntity
import com.demo.yourshoppingcart.ui.cart.CartViewModel

@Composable
fun ItemList(
    items: List<HomeEntity.ItemEntity>,
    onItemSelected: (itemId: String) -> Unit,
    quantityViewModel: QuantityViewModel,
    cartViewModel: CartViewModel
) {
    LazyColumn(
        modifier = Modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items) { item ->

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        enabled = true,
                        onClick = {
                            onItemSelected(item.id)
                        }),
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
                        model = item.img,
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
                            text = item.des,
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "$${item.price}",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    val cartId = cartViewModel.viewState.value.cartData?.cartId ?: ""

                    QuantityView(
                        productId = item.id,
                        viewModel = quantityViewModel
                    ) { quantity, onIncrease, onDecrease ->
                        if (quantity == 0) {
                            Button(
                                onClick = {
                                    onIncrease()
                                    val newQuantity = quantityViewModel.getQuantity(item.id)
                                    cartViewModel.updateCartQuantity(
                                        productId = item.id,
                                        newQuantity
                                    )
                                },
                                shape = RoundedCornerShape(50),
                                modifier = Modifier.height(40.dp)
                            ) {
                                Text(text = "Add")
                            }
                        } else {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(
                                    onClick = {
                                        onDecrease()
                                        val newQuantity = quantityViewModel.getQuantity(item.id)
                                        cartViewModel.updateCartQuantity(
                                            productId = item.id,
                                            newQuantity
                                        )
                                    },
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Remove,
                                        contentDescription = "Remove"
                                    )
                                }

                                Text(
                                    text = quantity.toString(),
                                    modifier = Modifier.width(24.dp),
                                    textAlign = TextAlign.Center
                                )

                                IconButton(
                                    onClick = {
                                        onIncrease()
                                        val newQuantity = quantityViewModel.getQuantity(item.id)
                                        cartViewModel.updateCartQuantity(
                                            item.id,
                                            newQuantity
                                        )
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Add"
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}