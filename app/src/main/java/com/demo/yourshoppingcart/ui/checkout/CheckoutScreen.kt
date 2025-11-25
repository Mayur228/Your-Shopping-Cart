package com.demo.yourshoppingcart.ui.checkout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material.icons.filled.Discount
import androidx.compose.material3.Button
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.demo.yourshoppingcart.common.ErrorView
import com.demo.yourshoppingcart.common.LoadingView
import com.demo.yourshoppingcart.payment.data.model.PaymentModel
import com.demo.yourshoppingcart.ui.cart.CartState
import com.demo.yourshoppingcart.ui.cart.CartViewModel
import com.demo.yourshoppingcart.ui.checkout.components.AddNewOptionCard
import com.demo.yourshoppingcart.ui.checkout.components.AppliedCouponCard
import com.demo.yourshoppingcart.ui.checkout.components.PaymentOptionItem
import com.demo.yourshoppingcart.ui.checkout.components.SummaryRow
import com.demo.yourshoppingcart.ui.checkout.dialog.AddCardDialog
import com.demo.yourshoppingcart.ui.checkout.dialog.AddUpiDialog
import com.demo.yourshoppingcart.ui.coupon.CouponsState
import com.demo.yourshoppingcart.ui.coupon.CouponsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    onBackClick: () -> Unit,
    cartViewModel: CartViewModel,
    couponViewModel: CouponsViewModel,
    onPlaceOrder: () -> Unit,
    onCoupons: () -> Unit,
) {
    val checkoutViewModel: CheckoutViewModel = hiltViewModel()
    val state by checkoutViewModel.viewState.collectAsState()

    val cartState by cartViewModel.viewState.collectAsState()
    val cartItems = (cartState as? CartState.Success)?.cartEntity?.cartItem ?: emptyList()

    val couponState by couponViewModel.viewState.collectAsState()
    val appliedCoupon = (couponState as? CouponsState.Success)?.coupons?.find { it.isApplied }

    val itemsTotal = cartItems.sumOf { it.productQun * (it.productPrice.toIntOrNull() ?: 0) }
    val deliveryCharge = 40
    val tax = (itemsTotal * 0.10).toInt()

    val discount = when {
        appliedCoupon == null -> 0
        appliedCoupon.discountAmount > 0 -> appliedCoupon.discountAmount
        appliedCoupon.discountPercent > 0 -> (itemsTotal * appliedCoupon.discountPercent) / 100
        else -> 0
    }

    val finalAmount = itemsTotal + deliveryCharge + tax - discount

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
                    item {
                        Text(
                            "Payment Method",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    // COD
                    item {
                        PaymentOptionItem(
                            title = "Cash on Delivery",
                            isSelected = data.selectedPaymentMethod == "COD",
                            onClick = { checkoutViewModel.selectPaymentMethod("COD") }
                        )
                    }

                    // UPI Header
                    item {
                        Text(
                            "UPI IDs",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

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
                    item {
                        AddNewOptionCard(
                            title = "Add UPI",
                            icon = Icons.Default.Add,
                            onClick = {
                                showAddUpiDialog = true
                            }
                        )
                    }

                    // Card Header
                    item {
                        Text(
                            "Cards",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                    // Card List
                    items(cardList.size) { index ->
                        val method = cardList[index] as PaymentModel.Card
                        PaymentOptionItem(
                            title = method.cardHolderName,
                            subtitle = "****${method.cardNumber.toString().takeLast(4)}",
                            isSelected = method.id == data.selectedPaymentMethod,
                            onClick = { checkoutViewModel.selectPaymentMethod(method.id) }
                        )
                    }

                    // Add Card
                    item {
                        AddNewOptionCard(
                            title = "Add Card",
                            icon = Icons.Default.AddCard,
                            onClick = {
                                showAddCardDialog = true
                            }
                        )
                    }

                    //Coupon
                    item {
                        Text(
                            "Coupon",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    item {
                        if (appliedCoupon != null) {
                            AppliedCouponCard(
                                code = appliedCoupon.code,
                                description = appliedCoupon.description,
                                onRemove = { couponViewModel.removeCoupon(appliedCoupon.id) }
                            )
                        } else {
                            AddNewOptionCard(
                                title = "Apply Coupon",
                                icon = Icons.Default.Discount,
                                onClick = {
                                    onCoupons()
                                }
                            )
                        }
                    }

                    item { Spacer(Modifier.height(12.dp)) }

                    // Order Summary
                    item {
                        Text(
                            "Order Summary",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    item {
                        SummaryRow(
                            "Items Total",
                            "₹$itemsTotal"
                        )
                    }
                    item {
                        SummaryRow(
                            "Delivery Charges",
                            "₹$deliveryCharge"
                        )
                    }
                    item {
                        SummaryRow(
                            "Tax (10%)",
                            "₹$tax"
                        )
                    }
                    if (appliedCoupon?.isApplied == true) item {
                        SummaryRow(
                            "Discount Applied ${appliedCoupon.code}",
                            "-₹${discount}"
                        )
                    }
                    item {
                        HorizontalDivider(
                            Modifier.padding(vertical = 12.dp),
                            DividerDefaults.Thickness,
                            DividerDefaults.color
                        )
                    }
                    item {
                        SummaryRow(
                            "Total Payable",
                            "₹$finalAmount",
                            bold = true
                        )
                    }
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