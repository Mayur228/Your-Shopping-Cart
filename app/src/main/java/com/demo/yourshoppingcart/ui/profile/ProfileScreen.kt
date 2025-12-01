package com.demo.yourshoppingcart.ui.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.demo.yourshoppingcart.common.ErrorView
import com.demo.yourshoppingcart.common.LoadingView
import com.demo.yourshoppingcart.payment.data.model.PaymentModel
import com.demo.yourshoppingcart.user.data.model.AddressModel

@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel,
    onLogout: () -> Unit
) {
    val state by profileViewModel.state.collectAsState()

    when(state) {
        is ProfileState.Error -> {
            ErrorView((state as ProfileState.Error).error)
        }
        ProfileState.Loading -> {
            LoadingView()
        }
        is ProfileState.Success -> {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // Payment Section
                item { Text("Payment Methods", style = MaterialTheme.typography.titleMedium) }
                items((state as ProfileState.Success).userDetails.paymentMethods) { payment ->
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
                            Text(
                                when (payment) {
                                    is PaymentModel.Card -> "Card ****${payment.cardNumber.toString().takeLast(4)}"
                                    is PaymentModel.Upi -> "UPI: ${payment.upiId}"
                                    else -> payment.id
                                },
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Row {
                                IconButton(onClick = { profileViewModel.updatePaymentMethod(payment.id,payment) }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Edit Payment")
                                }
                                IconButton(onClick = { profileViewModel.deletePaymentMethod(payment.id) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete Payment")
                                }
                            }
                        }
                    }
                }

                // Addresses Section
                item { Text("Addresses", style = MaterialTheme.typography.titleMedium) }
                items((state as ProfileState.Success).userDetails.address) { address ->
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
                                IconButton(onClick = { profileViewModel.updateAddress(address.id,address) }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Edit Address")
                                }
                                IconButton(onClick = { profileViewModel.deleteAddress(address.id) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete Address")
                                }
                            }
                        }
                    }
                }

                // Other Settings
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
                    Spacer(modifier = Modifier.height(16.dp))


                    ListItem(
                        headlineContent = { Text("Logout") },
                        leadingContent = { Icon(Icons.Default.ExitToApp, contentDescription = null) },
                        modifier = Modifier.clickable { onLogout() }
                    )
                }
            }
        }
    }
}