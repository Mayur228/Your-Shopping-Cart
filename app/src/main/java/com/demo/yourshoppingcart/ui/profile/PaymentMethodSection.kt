package com.demo.yourshoppingcart.ui.profile

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.demo.yourshoppingcart.payment.data.model.PaymentModel
import com.demo.yourshoppingcart.payment.domain.entity.paymentEntity

@Composable
fun PaymentMethodSection(
    paymentMethods: List<paymentEntity>,
    selectedMethod: String,
    onSelectMethod: (String) -> Unit,
    onEditMethod: (String, paymentEntity) -> Unit,
    onDeleteMethod: (String) -> Unit,
    onShowAddUpiDialog: () -> Unit,
    onShowAddCardDialog: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) } // 0=UPI, 1=Card

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .padding(16.dp)
    ) {

        // -----------------------------
        // TAB BAR (underline only)
        // -----------------------------
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.primary,
            indicator = {} // ❌ remove default indicator
        ) {
            TabItem("UPI", 0, selectedTab) { selectedTab = 0 }
            TabItem("Cards", 1, selectedTab) { selectedTab = 1 }
        }

        Spacer(Modifier.height(20.dp))

        // -----------------------------
        // TAB CONTENT
        // -----------------------------
        when (selectedTab) {

            // *************  UPI TAB  *************
            0 -> {
                val upiList = paymentMethods.filterIsInstance<PaymentModel.Upi>()

                upiList.forEach { upi ->
                    PaymentCard(
                        title = upi.upiId,
                        icon = Icons.Default.AccountBalanceWallet,
                        isSelected = selectedMethod == upi.id,
                        onSelect = { onSelectMethod(upi.id) },
                        onEdit = { onEditMethod(upi.id, upi) },
                        onDelete = { onDeleteMethod(upi.id) }
                    )
                    Spacer(Modifier.height(14.dp))
                }

                AddNewPaymentButton(
                    title = "Add UPI",
                    icon = Icons.Default.Add,
                    onClick = onShowAddUpiDialog
                )
            }

            // *************  CARD TAB  *************
            1 -> {
                val cardList = paymentMethods.filterIsInstance<PaymentModel.Card>()

                cardList.forEach { card ->
                    PaymentCard(
                        title = "${card.cardHolderName} ••••${card.cardNumber.toString().takeLast(4)}",
                        icon = Icons.Default.CreditCard,
                        isSelected = selectedMethod == card.id,
                        onSelect = { onSelectMethod(card.id) },
                        onEdit = { onEditMethod(card.id, card) },
                        onDelete = { onDeleteMethod(card.id) }
                    )
                    Spacer(Modifier.height(14.dp))
                }

                AddNewPaymentButton(
                    title = "Add Card",
                    icon = Icons.Default.AddCard,
                    onClick = onShowAddCardDialog
                )
            }
        }
    }
}

// -----------------------------
// PREMIUM TAB ITEM (Underline Only)
// -----------------------------
@Composable
private fun TabItem(
    title: String,
    index: Int,
    selectedTab: Int,
    onClick: () -> Unit
) {
    val isSelected = selectedTab == index

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .clickable { onClick() }
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(vertical = 12.dp),
            style = MaterialTheme.typography.titleMedium.copy(
                color = if (isSelected)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onSurfaceVariant
            )
        )

        // 🔥 Animated underline
        AnimatedVisibility(
            visible = isSelected,
            enter = expandHorizontally() + fadeIn(),
            exit = shrinkHorizontally() + fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .height(3.dp)
                    .width(28.dp)
                    .background(
                        MaterialTheme.colorScheme.primary,
                        RoundedCornerShape(50)
                    )
            )
        }
    }
}

// -----------------------------
// PAYMENT CARD
// -----------------------------
@Composable
private fun PaymentCard(
    title: String,
    icon: ImageVector,
    isSelected: Boolean,
    onSelect: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val scale by animateFloatAsState(if (isSelected) 1.03f else 1f)
    val bgColor =
        if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
        else MaterialTheme.colorScheme.surface

    val elevation = if (isSelected) 12.dp else 2.dp

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer(scaleX = scale, scaleY = scale)
            .shadow(elevation, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor)
            .clickable { onSelect() }
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {

            Icon(icon, null, tint = MaterialTheme.colorScheme.primary)

            Spacer(Modifier.width(12.dp))

            Text(
                title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )

            AnimatedVisibility(
                visible = isSelected,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Row {
                    IconButton(onClick = onEdit) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete",
                            tint = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}

// -----------------------------
// ADD NEW BUTTON
// -----------------------------
@Composable
private fun AddNewPaymentButton(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.10f))
            .clickable { onClick() }
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.width(12.dp))
        Text(
            title,
            style = MaterialTheme.typography.titleMedium.copy(
                color = MaterialTheme.colorScheme.primary
            )
        )
    }
}
