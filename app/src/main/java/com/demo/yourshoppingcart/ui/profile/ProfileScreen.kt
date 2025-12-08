package com.demo.yourshoppingcart.ui.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.demo.yourshoppingcart.common.ErrorView
import com.demo.yourshoppingcart.common.LoadingView
import com.demo.yourshoppingcart.payment.data.model.PaymentModel
import com.demo.yourshoppingcart.payment.domain.entity.paymentEntity
import com.demo.yourshoppingcart.ui.checkout.components.PaymentOptionItem
import com.demo.yourshoppingcart.ui.checkout.dialog.AddCardDialog
import com.demo.yourshoppingcart.ui.checkout.dialog.AddUpiDialog
import com.demo.yourshoppingcart.user.data.model.AddressModel

@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel,
    onLogout: () -> Unit
) {
    val state by profileViewModel.state.collectAsState()

    var showAddUpiDialog by remember { mutableStateOf(false) }
    var showAddCardDialog by remember { mutableStateOf(false) }

    when (state) {
        is ProfileState.Error -> ErrorView((state as ProfileState.Error).error)
        ProfileState.Loading -> LoadingView()
        is ProfileState.Success -> {
            val user = (state as ProfileState.Success).userDetails

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // Payment Methods Section
                item { Text("Payment Methods", style = MaterialTheme.typography.titleMedium) }
                item {
                    PaymentMethodSection(
                        paymentMethods = user.paymentMethods,
                        selectedMethod = user.selectedPaymentMethod,
                        onSelectMethod = { profileViewModel.selectPaymentMethod(it) },
                        onEditMethod = { id, method -> profileViewModel.updatePaymentMethod(id, method) },
                        onDeleteMethod = { profileViewModel.deletePaymentMethod(it) },
                        onShowAddUpiDialog = { showAddUpiDialog = true },
                        onShowAddCardDialog = { showAddCardDialog = true }
                    )
                }

                // Addresses Section
                item { Text("Addresses", style = MaterialTheme.typography.titleMedium) }
                items(user.address) { address ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(address.name, style = MaterialTheme.typography.bodyLarge)
                                Text(address.fullAddress, style = MaterialTheme.typography.bodyMedium)
                            }
                            Row {
                                IconButton(onClick = { profileViewModel.updateAddress(address.id, address) }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Edit Address")
                                }
                                IconButton(onClick = { profileViewModel.deleteAddress(address.id) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete Address")
                                }
                            }
                        }
                    }
                }

                // Logout Section
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    ListItem(
                        headlineContent = { Text("Logout") },
                        leadingContent = { Icon(Icons.Default.ExitToApp, contentDescription = null) },
                        modifier = Modifier.clickable { onLogout() }
                    )
                }
            }

            // Dialogs for Adding Payment Methods
            if (showAddUpiDialog) AddUpiDialog(
                onDismiss = { showAddUpiDialog = false },
                onAdd = { upi ->
                    profileViewModel.addPaymentMethod(PaymentModel.Upi(upiId = upi))
                    showAddUpiDialog = false
                }
            )

            if (showAddCardDialog) AddCardDialog(
                onDismiss = { showAddCardDialog = false },
                onAdd = { cardData ->
                    profileViewModel.addPaymentMethod(cardData)
                    showAddCardDialog = false
                }
            )
        }
    }
}

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
    var expanded by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {

            // Header Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val selectedTitle = paymentMethods.find { it.id == selectedMethod }?.let {
                    when (it) {
                        is PaymentModel.Card -> "Card ****${it.cardNumber.toString().takeLast(4)}"
                        is PaymentModel.Upi -> it.upiId
                        else -> it.id
                    }
                } ?: "Cash on Delivery"

                Text("Selected: $selectedTitle", style = MaterialTheme.typography.bodyMedium)
                Icon(
                    imageVector = if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                    contentDescription = "Expand"
                )
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))

                // COD Option
                PaymentOptionItem(
                    title = "Cash on Delivery",
                    isSelected = selectedMethod == "COD",
                    onClick = { onSelectMethod("COD") }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // UPI Methods
                paymentMethods.filterIsInstance<PaymentModel.Upi>().forEach { method ->
                    PaymentOptionItem(
                        title = method.upiId,
                        isSelected = selectedMethod == method.id,
                        onClick = { onSelectMethod(method.id) },
                       // onEdit = { onEditMethod(method.id, method) },
                        //onDelete = { onDeleteMethod(method.id) }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Card Methods
                paymentMethods.filterIsInstance<PaymentModel.Card>().forEach { method ->
                    PaymentOptionItem(
                        title = method.cardHolderName,
                        subtitle = "****${method.cardNumber.toString().takeLast(4)}",
                        isSelected = selectedMethod == method.id,
                        onClick = { onSelectMethod(method.id) },
                       // onEdit = { onEditMethod(method.id, method) },
                        //onDelete = { onDeleteMethod(method.id) }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Add New Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(onClick = onShowAddUpiDialog, modifier = Modifier.weight(1f)) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Spacer(Modifier.width(4.dp))
                        Text("Add UPI")
                    }
                    Button(onClick = onShowAddCardDialog, modifier = Modifier.weight(1f)) {
                        Icon(Icons.Default.AddCard, contentDescription = null)
                        Spacer(Modifier.width(4.dp))
                        Text("Add Card")
                    }
                }
            }
        }
    }
}
