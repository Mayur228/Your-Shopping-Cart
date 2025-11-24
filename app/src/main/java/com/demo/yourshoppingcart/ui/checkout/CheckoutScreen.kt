package com.demo.yourshoppingcart.ui.checkout

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.demo.yourshoppingcart.common.ErrorView
import com.demo.yourshoppingcart.common.LoadingView
import com.demo.yourshoppingcart.payment.data.model.PaymentModel
import com.demo.yourshoppingcart.ui.cart.CartState
import com.demo.yourshoppingcart.ui.cart.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    onBackClick: () -> Unit,
    viewModel: CartViewModel,
    onPlaceOrder: () -> Unit,
) {
    val checkoutViewModel: CheckoutViewModel = hiltViewModel()
    val state by checkoutViewModel.viewState.collectAsState()

    val cartState by viewModel.viewState.collectAsState()
    val cartItems = (cartState as? CartState.Success)?.cartEntity?.cartItem ?: emptyList()

    val itemsTotal = cartItems.sumOf { it.productQun * (it.productPrice.toIntOrNull() ?: 0) }
    val deliveryCharge = 40
    val tax = (itemsTotal * 0.10).toInt()
    val finalAmount = itemsTotal + deliveryCharge + tax

    var showAddUpiDialog by remember { mutableStateOf(false) }
    var showAddCardDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Checkout") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            Button(
                onClick = onPlaceOrder,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(54.dp),
                shape = RoundedCornerShape(50)
            ) {
                Text("Place Order (₹$finalAmount)")
            }
        }
    ) { padding ->

        when (state) {
            is CheckoutState.Loading -> LoadingView()
            is CheckoutState.Error -> ErrorView((state as CheckoutState.Error).error)

            is CheckoutState.Success -> {
                val data = state as CheckoutState.Success
                val paymentMethods = data.typesOfPaymentMethods

                val upiList = paymentMethods.filter { it.type.equals("UPI", ignoreCase = true) }
                val cardList = paymentMethods.filter { it.type.equals("CARD", ignoreCase = true) }

                LazyColumn(
                    modifier = Modifier
                        .padding(padding)
                        .padding(16.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Payment Methods Header
                    item { Text("Payment Method", style = MaterialTheme.typography.titleMedium) }

                    // COD
                    item {
                        PaymentOptionItem(
                            title = "Cash on Delivery",
                            isSelected = data.selectedPaymentMethod == "COD",
                            onClick = { checkoutViewModel.selectPaymentMethod("COD") }
                        )
                    }

                    // UPI Header
                    item { Text("UPI IDs", style = MaterialTheme.typography.bodyLarge) }

                    // UPI List
                    items(upiList.size) { index ->
                        val method = upiList[index] as PaymentModel.Upi
                        PaymentOptionItem(
                            title = method.upiId,
                            isSelected = method.id == data.selectedPaymentMethod,
                            onClick = { checkoutViewModel.selectPaymentMethod(method.id) }
                        )
                    }

                    // Add UPI
                    item { AddNewOptionCard(title = "Add UPI") { showAddUpiDialog = true } }

                    // Card Header
                    item { Text("Cards", style = MaterialTheme.typography.bodyLarge) }

                    // Card List
                    items(cardList.size) { index ->
                        val method = cardList[index] as PaymentModel.Card
                        PaymentOptionItem(
                            title = method.cardHolderName,
                            subtitle = "****${ method.cardNumber.toString().takeLast(4) }",
                            isSelected = method.id == data.selectedPaymentMethod,
                            onClick = { checkoutViewModel.selectPaymentMethod(method.id) }
                        )
                    }

                    // Add Card
                    item { AddNewOptionCard(title = "Add Card") { showAddCardDialog = true } }

                    // Order Summary
                    item { Spacer(Modifier.height(12.dp)) }
                    item { Text("Order Summary", style = MaterialTheme.typography.titleMedium) }
                    item { SummaryRow("Items Total", "₹$itemsTotal") }
                    item { SummaryRow("Delivery Charges", "₹$deliveryCharge") }
                    item { SummaryRow("Tax (10%)", "₹$tax") }
                    item { Divider(Modifier.padding(vertical = 12.dp)) }
                    item { SummaryRow("Total Payable", "₹$finalAmount", bold = true) }
                }

                // Dialogs
                if (showAddUpiDialog) AddUpiDialog(
                    onDismiss = { showAddUpiDialog = false },
                    onAdd = { upi -> checkoutViewModel.addPaymentMethod(PaymentModel.Upi(upiId = upi)) }
                )

                if (showAddCardDialog) AddCardDialog(
                    onDismiss = { showAddCardDialog = false },
                    onAdd = { cardData -> checkoutViewModel.addPaymentMethod(cardData) }
                )
            }
        }
    }
}

// Unified Payment Option Item
@Composable
fun PaymentOptionItem(
    title: String,
    subtitle: String? = null,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Checkbox / Icon
            Icon(
                imageVector = if (isSelected) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                contentDescription = null,
                tint = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray
            )

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                // Title always shown
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                // Subtitle shown only for cards
                subtitle?.let {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
fun AddNewOptionCard(title: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(Modifier.width(12.dp))
            Text(title, style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Composable
fun SummaryRow(label: String, value: String, bold: Boolean = false) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            label,
            style = if (bold) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyLarge
        )
        Text(
            value,
            style = if (bold) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun AddUpiDialog(
    onDismiss: () -> Unit,
    onAdd: (String) -> Unit
) {
    var upiId by remember { mutableStateOf("") }
    val isValid = upiId.contains("@")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add UPI ID") },
        text = {
            Column {
                Text("Enter your valid UPI ID (example: name@okaxis)")
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(
                    value = upiId,
                    onValueChange = { upiId = it },
                    label = { Text("UPI ID") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onAdd(upiId) },
                enabled = isValid
            ) { Text("Add") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun AddCardDialog(
    onDismiss: () -> Unit,
    onAdd: (PaymentModel.Card) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var cardNumber by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    var expiry by remember { mutableStateOf("") }
    var nickname by remember { mutableStateOf("") }

    // Expiry input handler
    fun formatExpiry(input: String): String {
        val digits = input.filter { it.isDigit() }
        return when {
            digits.length >= 3 -> "${digits.take(2)}/${digits.drop(2).take(2)}"
            digits.length >= 1 -> digits
            else -> ""
        }
    }

    val isCardNumberValid = cardNumber.length == 16 && cardNumber.all { it.isDigit() }
    val isCvvValid = cvv.length == 3 && cvv.all { it.isDigit() }
    val isExpiryValid = expiry.matches(Regex("(0[1-9]|1[0-2])/([0-9]{2})")) // MM/YY
    val isNameValid = name.isNotBlank()

    val isValid = isCardNumberValid && isCvvValid && isExpiryValid && isNameValid

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Card") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Card Holder Name") },
                    singleLine = true,
                    isError = !isNameValid && name.isNotEmpty()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = cardNumber,
                    onValueChange = {
                        if (it.length <= 16 && it.all { ch -> ch.isDigit() }) cardNumber = it
                    },
                    label = { Text("Card Number") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = !isCardNumberValid && cardNumber.isNotEmpty()
                )
                Spacer(Modifier.height(8.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    OutlinedTextField(
                        value = cvv,
                        onValueChange = { if (it.length <= 3 && it.all { ch -> ch.isDigit() }) cvv = it },
                        label = { Text("CVV") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = !isCvvValid && cvv.isNotEmpty()
                    )
                    Spacer(Modifier.width(8.dp))
                    OutlinedTextField(
                        value = expiry,
                        onValueChange = { expiry = formatExpiry(it) },
                        label = { Text("Expiry (MM/YY)") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = !isExpiryValid && expiry.isNotEmpty()
                    )
                }
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = nickname,
                    onValueChange = { nickname = it },
                    label = { Text("Nickname (Optional)") },
                    singleLine = true
                )

                if (!isValid && (cardNumber.isNotEmpty() || cvv.isNotEmpty() || expiry.isNotEmpty())) {
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = "Please check all fields are correct: Card 16 digits, CVV 3 digits, Expiry MM/YY",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onAdd(
                        PaymentModel.Card(
                            cardHolderName = name,
                            cardNumber = cardNumber.toLong(),
                            cvv = cvv.toInt(),
                            expiryDate = expiry,
                            nickName = nickname
                        )
                    )
                },
                enabled = isValid
            ) { Text("Add") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}