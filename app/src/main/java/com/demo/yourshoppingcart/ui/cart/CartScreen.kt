package com.demo.yourshoppingcart.ui.cart

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Discount
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material.icons.filled.AddHome
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.DeliveryDining
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.demo.yourshoppingcart.common.AddAddressDialog
import com.demo.yourshoppingcart.payment.data.model.PaymentModel
import com.demo.yourshoppingcart.ui.checkout.CheckoutViewModel
import com.demo.yourshoppingcart.ui.cart.components.AddNewOptionCard
import com.demo.yourshoppingcart.ui.cart.components.AppliedCouponCard
import com.demo.yourshoppingcart.ui.cart.components.EmptyCartView
import com.demo.yourshoppingcart.ui.cart.components.PaymentOptionItem
import com.demo.yourshoppingcart.ui.cart.components.PaymentSection
import com.demo.yourshoppingcart.ui.cart.components.SummaryRow
import com.demo.yourshoppingcart.ui.cart.dialog.AddCardDialog
import com.demo.yourshoppingcart.ui.cart.dialog.AddUpiDialog
import com.demo.yourshoppingcart.ui.coupon.CouponsState
import com.demo.yourshoppingcart.ui.coupon.CouponsViewModel
import com.demo.yourshoppingcart.user.data.model.AddressModel
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
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

    // Dialog states
    var showAddUpiDialog by remember { mutableStateOf(false) }
    var showAddCardDialog by remember { mutableStateOf(false) }
    var showAddAddressDialog by remember { mutableStateOf(false) }
    var expandedPayment by remember { mutableStateOf(false) }

    // Empty cart
    if (cartItems.isEmpty()) {
        EmptyCartView()
        return
    }

    // Price calculations
    val itemsTotal = cartItems.sumOf { it.productQun * (it.productPrice.toIntOrNull() ?: 0) }
    val deliveryCharge = 40
    val tax = (itemsTotal * 0.10).roundToInt()
    val appliedCoupon = (couponState as? CouponsState.Success)?.coupons?.find { it.isApplied }
    val discount = when {
        appliedCoupon == null -> 0
        appliedCoupon.discountAmount > 0 -> appliedCoupon.discountAmount
        appliedCoupon.discountPercent > 0 -> (itemsTotal * appliedCoupon.discountPercent) / 100
        else -> 0
    }
    val finalAmount = itemsTotal + deliveryCharge + tax - discount

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Your Cart") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = "Back")
                    }
                },
                actions = {
                    // placeholder spacing
                    Spacer(modifier = Modifier.width(8.dp))
                }
            )
        },
        bottomBar = {
            // Sticky bottom summary bar
            Surface(
                tonalElevation = 8.dp,
                shadowElevation = 10.dp
            ) {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Total Payable", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onBackground)
                            Text("₹$finalAmount",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Button(
                            onClick = {
                                if (isPhoneLogin) onPlaceOrder() else navigateToPhoneLogin()
                            },
                            shape = RoundedCornerShape(50),
                            modifier = Modifier.height(48.dp)
                        ) {
                            Text(if (isPhoneLogin) "Place Order" else "Login to Continue")
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("${cartItems.size} items", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("Includes delivery & taxes", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        },
        content = { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 100.dp) // allow space for sticky bar
            ) {

                items(cartItems, key = { it.productId }) { item ->
                    ProductItemCardAPlus(
                        itemId = item.productId,
                        itemName = item.productName,
                        itemPrice = item.productPrice,
                        itemDes = item.productDes,
                        itemImg = item.productImg,
                        quantity = item.productQun,
                        onIncrease = { cartViewModel.createOrUpdateCart(item.productId, item.productQun + 1) },
                        onDecrease = { cartViewModel.createOrUpdateCart(item.productId, item.productQun - 1) }
                    )
                }

                // Coupon section
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

                // Address
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            if (addresses.isEmpty()) {
                                AddNewOptionCard(
                                    title = "Add Address",
                                    icon = Icons.Default.AddHome,
                                    onClick = { showAddAddressDialog = true }
                                )
                            } else {
                                Text("Deliver to", style = MaterialTheme.typography.labelLarge)
                                Spacer(modifier = Modifier.height(8.dp))
                                addresses.forEach { addr ->
                                    Text(
                                        text = addr.fullAddress,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                }
                            }
                        }
                    }
                }

                // Payment
                if (checkoutState is CheckoutState.Success) {
                    val data = checkoutState as CheckoutState.Success
                    item {
                        PaymentSection(
                            checkoutState = checkoutState,
                            onSelect = { checkoutViewModel.selectPaymentMethod(it) },
                            onAddUpi = { showAddUpiDialog = true },
                            onAddCard = { showAddCardDialog = true }
                        )
                    }
                }

                // Order summary
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Order Summary", style = MaterialTheme.typography.titleMedium)
                            Spacer(modifier = Modifier.height(12.dp))

                            SummaryRow("Items Total", "₹$itemsTotal",icon = Icons.Default.CurrencyRupee)
                            SummaryRow("Delivery", "₹$deliveryCharge",icon = Icons.Default.DeliveryDining)
                            SummaryRow("Tax (10%)", "₹$tax",icon = Icons.Default.Info)
                            if (discount > 0) SummaryRow("Discount", "-₹$discount", icon = Icons.Default.Discount)

                            Divider(modifier = Modifier.padding(vertical = 8.dp))

                            SummaryRow("Total Payable", "₹$finalAmount", bold = true)
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(12.dp)) }
            }
        }
    )

    // Dialogs
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
        onAdd = { /* handle add address */ }
    )
}

/* ---------------------------
   Product card: A+ Ribbon style
   --------------------------- */
@Composable
private fun ProductItemCardAPlus(
    itemId: String,
    itemName: String,
    itemPrice: String,
    itemDes: String,
    itemImg: String,
    quantity: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit
) {
    var wish by remember { mutableStateOf(false) }
    // Fancy discount percent derived from id for demo; replace with real value when available
    val discount = remember(itemId) { (5..30).random() }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(14.dp)),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // IMAGE + ribbon
                Box(modifier = Modifier.size(110.dp)) {
                    Image(
                        painter = rememberAsyncImagePainter(itemImg),
                        contentDescription = itemName,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(10.dp))
                    )

                    // subtle overlay for legibility
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(
                                Brush.verticalGradient(
                                    0f to Color.Transparent,
                                    1f to Color.Black.copy(alpha = 0.06f)
                                )
                            )
                    )

                    // Ribbon
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .offset(x = (-6).dp, y = (-6).dp)
                            .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(text = "-$discount%", style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Bold))
                    }

                    // wishlist floating heart
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = 6.dp, y = (-6).dp)
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceContainer)
                            .clickable { wish = !wish },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (wish) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Wishlist",
                            tint = if (wish) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(itemName, style = MaterialTheme.typography.titleMedium, maxLines = 2, overflow = TextOverflow.Ellipsis)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("₹$itemPrice", style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold))
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(itemDes, style = MaterialTheme.typography.bodySmall, maxLines = 2, overflow = TextOverflow.Ellipsis, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Floating quantity vertical pill
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    AnimatedContent(targetState = quantity, transitionSpec = { fadeIn(tween(200)) + expandVertically() togetherWith fadeOut(tween(150)) }) { qty ->
                        if (qty <= 0) {
                            Button(onClick = onIncrease, shape = RoundedCornerShape(50)) {
                                Icon(Icons.Default.Add, contentDescription = "Add")
                            }
                        } else {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                IconButton(onClick = onIncrease, modifier = Modifier.size(36.dp)) {
                                    Icon(Icons.Default.Add, contentDescription = "Increase")
                                }
                                Text(qty.toString(), style = MaterialTheme.typography.titleMedium)
                                IconButton(onClick = onDecrease, modifier = Modifier.size(36.dp)) {
                                    Icon(Icons.Default.Remove, contentDescription = "Decrease")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
