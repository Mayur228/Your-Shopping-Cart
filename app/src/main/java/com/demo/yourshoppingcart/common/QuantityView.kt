package com.demo.yourshoppingcart.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember

@Composable
fun QuantityView(
    productId: String,
    viewModel: QuantityViewModel,
    content: @Composable (quantity: Int, onIncrease: () -> Unit, onDecrease: () -> Unit) -> Unit
) {
    val quantity by remember { derivedStateOf { viewModel.getQuantity(productId) } }

    content(
        quantity,
        { viewModel.increaseQuantity(productId) },
        { viewModel.decreaseQuantity(productId) }
    )
}