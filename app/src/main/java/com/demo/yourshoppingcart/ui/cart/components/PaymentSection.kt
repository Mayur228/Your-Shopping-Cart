package com.demo.yourshoppingcart.ui.cart.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.demo.yourshoppingcart.payment.data.model.PaymentModel
import com.demo.yourshoppingcart.ui.cart.CheckoutState

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PaymentSection(
    checkoutState: CheckoutState,
    onSelect: (String) -> Unit,
    onAddUpi: () -> Unit,
    onAddCard: () -> Unit
) {
    if (checkoutState !is CheckoutState.Success) return
    val data = checkoutState

    var expanded by remember { mutableStateOf(false) }

    val selectedLabel = when (val method =
        data.typesOfPaymentMethods.find { it.id == data.selectedPaymentMethod }) {
        is PaymentModel.Upi -> method.upiId
        is PaymentModel.Card -> "Card ••••${method.cardNumber.toString().takeLast(4)}"
        PaymentModel.COD -> "Cash on Delivery"
        else -> "Cash on Delivery"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // HEADER
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column {
                    Text(
                        "Payment Method",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        selectedLabel,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            // CONTENT
            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(animationSpec = tween(350)) + fadeIn(),
                exit = shrinkVertically(animationSpec = tween(250)) + fadeOut()
            ) {
                Column(modifier = Modifier.padding(top = 16.dp)) {

                    // COD
                    PaymentSectionItem(
                        title = "Cash on Delivery",
                        icon = Icons.Default.Money,
                        isSelected = data.selectedPaymentMethod == "COD",
                        onClick = { onSelect("COD") }
                    )

                    Spacer(Modifier.height(16.dp))

                    // UPI
                    data.typesOfPaymentMethods.filterIsInstance<PaymentModel.Upi>().forEach { upi ->
                        PaymentSectionItem(
                            title = upi.upiId,
                            icon = Icons.Default.AccountBalanceWallet,
                            isSelected = data.selectedPaymentMethod == upi.id,
                            onClick = { onSelect(upi.id) }
                        )
                        Spacer(Modifier.height(16.dp))
                    }

                    AddNewOptionCard(
                        title = "Add UPI",
                        icon = Icons.Default.Add,
                        onClick = onAddUpi
                    )

                    Spacer(Modifier.height(22.dp))

                    // CARDS
                    data.typesOfPaymentMethods.filterIsInstance<PaymentModel.Card>().forEach { card ->
                        PaymentSectionItem(
                            title = "${card.cardHolderName} ••••${card.cardNumber.toString().takeLast(4)}",
                            icon = Icons.Default.CreditCard,
                            isSelected = data.selectedPaymentMethod == card.id,
                            onClick = { onSelect(card.id) }
                        )
                        Spacer(Modifier.height(16.dp))
                    }

                    AddNewOptionCard(
                        title = "Add Card",
                        icon = Icons.Default.AddCard,
                        onClick = onAddCard
                    )
                }
            }
        }
    }
}

@Composable
private fun PaymentSectionItem(
    title: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val animatedAlpha by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0.35f,
        animationSpec = tween(400)
    )

    val scale by animateFloatAsState(
        if (isSelected) 1.02f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy)
    )

    val neonBarColor by animateColorAsState(
        if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
        tween(300)
    )

    val backgroundColor by animateColorAsState(
        if (isSelected)
            MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
        else
            MaterialTheme.colorScheme.surfaceContainerLow,
        tween(350)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                this.scaleX = scale
                this.scaleY = scale
            }
            .shadow(
                elevation = if (isSelected) 10.dp else 2.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
            )
            .background(backgroundColor, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // Neon Left Bar
        Box(
            modifier = Modifier
                .width(5.dp)
                .height(44.dp)
                .clip(RoundedCornerShape(50))
                .background(neonBarColor)
        )

        Spacer(Modifier.width(14.dp))

        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary.copy(alpha = animatedAlpha)
        )

        Spacer(Modifier.width(14.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = animatedAlpha),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
