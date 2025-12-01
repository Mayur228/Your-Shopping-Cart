package com.demo.yourshoppingcart.ui.cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material.icons.filled.AddHome
import androidx.compose.material.icons.filled.Discount
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.demo.yourshoppingcart.common.AddAddressDialog
import com.demo.yourshoppingcart.payment.data.model.PaymentModel
import com.demo.yourshoppingcart.ui.cart.CheckoutState
import com.demo.yourshoppingcart.ui.checkout.CheckoutViewModel
import com.demo.yourshoppingcart.ui.checkout.components.AddNewOptionCard
import com.demo.yourshoppingcart.ui.checkout.components.AppliedCouponCard
import com.demo.yourshoppingcart.ui.checkout.components.PaymentOptionItem
import com.demo.yourshoppingcart.ui.checkout.components.SummaryRow
import com.demo.yourshoppingcart.ui.checkout.dialog.AddCardDialog
import com.demo.yourshoppingcart.ui.checkout.dialog.AddUpiDialog
import com.demo.yourshoppingcart.ui.coupon.CouponsState
import com.demo.yourshoppingcart.ui.coupon.CouponsViewModel
import com.demo.yourshoppingcart.user.data.model.AddressModel

@Composable
fun CartScreen(
    cartViewModel: CartViewModel,
    couponViewModel: CouponsViewModel,
    onApplyCoupon: () -> Unit,
    onPlaceOrder: () -> Unit,
    onBackClick: () -> Unit,
    addresses: List<AddressModel> = emptyList(),
    isPhoneLogin: Boolean,
    navigateToPhoneLogin: () -> Unit,
) {
    val cartState by cartViewModel.viewState.collectAsState()
    val couponState by couponViewModel.viewState.collectAsState()
    val checkoutViewModel: CheckoutViewModel = hiltViewModel()
    val checkoutState by checkoutViewModel.viewState.collectAsState()

    val cartItems = (cartState as? CartState.Success)?.cartEntity?.cartItem ?: emptyList()
    val appliedCoupon = (couponState as? CouponsState.Success)?.coupons?.find { it.isApplied }

    // TOTALS
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
    var showAddAddressDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(8.dp)
    ) {
        // PRODUCT ITEMS
        items(cartItems, key = { it.productId }) { item ->
            ProductItemCard(
                itemName = item.productName,
                itemPrice = item.productPrice,
                itemDes = item.productDes,
                itemImg = item.productImg,
                quantity = item.productQun,
                onIncrease = { cartViewModel.createOrUpdateCart(item.productId, item.productQun + 1) },
                onDecrease = { cartViewModel.createOrUpdateCart(item.productId, item.productQun - 1) }
            )
        }

        // COUPON SECTION
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
                    onClick = onApplyCoupon
                )
            }
        }

        // ADDRESS SECTION
        item {
            if (addresses.isEmpty()) {
                AddNewOptionCard(
                    title = "Add Address",
                    icon = Icons.Default.AddHome,
                    onClick = { showAddAddressDialog = true }
                )
            } else {
                addresses.forEach { addr ->
                    Text("Deliver to: ${addr.fullAddress}", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }

        // PAYMENT METHOD SECTION
        if (checkoutState is CheckoutState.Success) {
            val data = checkoutState as CheckoutState.Success
            item {
                PaymentMethodSection(
                    checkoutViewModel = checkoutViewModel,
                    selectedMethod = data.selectedPaymentMethod,
                    onShowAddUpiDialog = { showAddUpiDialog = true },
                    onShowAddCardDialog = { showAddCardDialog = true }
                )
            }
        }

        // ORDER SUMMARY + PLACE ORDER as last item
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Order Summary", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(12.dp))
                    SummaryRow("Items Total", "₹$itemsTotal")
                    SummaryRow("Delivery", "₹$deliveryCharge")
                    SummaryRow("Tax (10%)", "₹$tax")
                    if (discount > 0) SummaryRow("Discount", "-₹$discount")
                    Divider(Modifier.padding(vertical = 8.dp))
                    SummaryRow("Total Payable", "₹$finalAmount", bold = true)
                    Spacer(Modifier.height(8.dp))

                    Button(
                        onClick = { if (isPhoneLogin) onPlaceOrder() else navigateToPhoneLogin() },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(if (isPhoneLogin) "Place Order (₹$finalAmount)" else "Login to Continue")
                    }
                }
            }
        }
    }

    // ADD DIALOGS
    if (showAddUpiDialog) AddUpiDialog(
        onDismiss = { showAddUpiDialog = false },
        onAdd = { upi -> checkoutViewModel.addPaymentMethod(PaymentModel.Upi(upiId = upi)) }
    )

    if (showAddCardDialog) AddCardDialog(
        onDismiss = { showAddCardDialog = false },
        onAdd = { cardData -> checkoutViewModel.addPaymentMethod(cardData) }
    )

    if (showAddAddressDialog) AddAddressDialog(
        onDismiss = { showAddAddressDialog = false },
        onAdd = {
            //add address
        }
    )
}

@Composable
fun ProductItemCard(
    itemName: String,
    itemPrice: String,
    itemDes: String,
    itemImg: String,
    quantity: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(itemImg),
                contentDescription = itemName,
                modifier = Modifier
                    .size(72.dp)
                    .background(Color.White, RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(itemName, style = MaterialTheme.typography.titleMedium)
                Text("₹$itemPrice", style = MaterialTheme.typography.bodyLarge)
                Text(itemDes, style = MaterialTheme.typography.bodySmall, maxLines = 2, color = Color.Gray)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onDecrease) { Icon(Icons.Default.Remove, contentDescription = "Decrease") }
                Text(quantity.toString())
                IconButton(onClick = onIncrease) { Icon(Icons.Default.Add, contentDescription = "Increase") }
            }
        }
    }
}

@Composable
fun PaymentMethodSection(
    checkoutViewModel: CheckoutViewModel,
    selectedMethod: String,
    onShowAddUpiDialog: () -> Unit,
    onShowAddCardDialog: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val checkoutState by checkoutViewModel.viewState.collectAsState()
    val data = checkoutState as? CheckoutState.Success ?: return

    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val selectedTitle = data.typesOfPaymentMethods.find { it.id == selectedMethod }?.let {
                    when (it) {
                        is PaymentModel.Card -> "Card ****${it.cardNumber.toString().takeLast(4)}"
                        is PaymentModel.Upi -> it.upiId
                        else -> it.id
                    }
                } ?: "Cash on Delivery"

                Text("Payment Method: $selectedTitle", style = MaterialTheme.typography.bodyMedium)
                Icon(
                    imageVector = if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                    contentDescription = "Expand"
                )
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))

                // COD
                PaymentOptionItem(
                    title = "Cash on Delivery",
                    isSelected = selectedMethod == "COD",
                    onClick = { checkoutViewModel.selectPaymentMethod("COD") }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // UPI Section
                data.typesOfPaymentMethods.filterIsInstance<PaymentModel.Upi>().forEach { method ->
                    PaymentOptionItem(
                        title = method.upiId,
                        isSelected = selectedMethod == method.id,
                        onClick = { checkoutViewModel.selectPaymentMethod(method.id) }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Card Section
                data.typesOfPaymentMethods.filterIsInstance<PaymentModel.Card>().forEach { method ->
                    PaymentOptionItem(
                        title = method.cardHolderName,
                        subtitle = "****${method.cardNumber.toString().takeLast(4)}",
                        isSelected = selectedMethod == method.id,
                        onClick = { checkoutViewModel.selectPaymentMethod(method.id) }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Buttons row: Add UPI and Add Card
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AddNewOptionCard(
                        title = "Add UPI",
                        icon = Icons.Default.Add,
                        onClick = onShowAddUpiDialog,
                        modifier = Modifier.weight(1f)
                    )
                    AddNewOptionCard(
                        title = "Add Card",
                        icon = Icons.Default.AddCard,
                        onClick = onShowAddCardDialog,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}