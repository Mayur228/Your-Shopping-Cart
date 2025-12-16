package com.demo.yourshoppingcart.ui.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.demo.yourshoppingcart.common.ErrorView
import com.demo.yourshoppingcart.common.LoadingView
import com.demo.yourshoppingcart.payment.data.model.PaymentModel
import com.demo.yourshoppingcart.ui.cart.dialog.AddCardDialog
import com.demo.yourshoppingcart.ui.cart.dialog.AddUpiDialog
import com.demo.yourshoppingcart.ui.profile.components.UserHeader

@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel,
    onLogout: () -> Unit
) {
    val state by profileViewModel.state.collectAsState()

    var showAddUpiDialog by remember { mutableStateOf(false) }
    var showAddCardDialog by remember { mutableStateOf(false) }
    var showAddAddressDialog by remember { mutableStateOf(false) }

    when (state) {

        is ProfileState.Loading -> LoadingView()

        is ProfileState.Error -> ErrorView((state as ProfileState.Error).error)

        is ProfileState.Success -> {
            val user = (state as ProfileState.Success).userDetails

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                item {
                    UserHeader(
                        name = user.userName,
                        phone = user.userNum,
                        avatar = user.userPP
                    )
                }

                item { SectionTitle("Payment Methods") }

                item {
                    PaymentMethodSection(
                        paymentMethods = user.paymentMethods,
                        selectedMethod = user.selectedPaymentMethod,
                        onSelectMethod = { profileViewModel.selectPaymentMethod(it) },
                        onEditMethod = { id, method ->
                            profileViewModel.updatePaymentMethod(
                                id,
                                method
                            )
                        },
                        onDeleteMethod = { profileViewModel.deletePaymentMethod(it) },
                        onShowAddUpiDialog = { showAddUpiDialog = true },
                        onShowAddCardDialog = { showAddCardDialog = true }
                    )
                }

                item { SectionTitle("Your Addresses") }

                if (user.address.isEmpty()) {
                    item { AddAddressButton { showAddAddressDialog = true } }
                } else {
                    items(user.address) { address ->
                        AddressCard(
                            address = address,
                            onEdit = { profileViewModel.updateAddress(address.id, address) },
                            onDelete = { profileViewModel.deleteAddress(address.id) }
                        )
                    }
                    item { AddAddressButton { showAddAddressDialog = true } }
                }

                item { LogoutCard(onLogout = onLogout) }
            }

            if (showAddUpiDialog) {
                AddUpiDialog(
                    onDismiss = { showAddUpiDialog = false },
                    onAdd = { upi ->
                        profileViewModel.addPaymentMethod(PaymentModel.Upi(upiId = upi))
                        showAddUpiDialog = false
                    }
                )
            }

            if (showAddCardDialog) {
                AddCardDialog(
                    onDismiss = { showAddCardDialog = false },
                    onAdd = { card ->
                        profileViewModel.addPaymentMethod(card)
                        showAddCardDialog = false
                    }
                )
            }
        }
    }
}
