package com.demo.yourshoppingcart.ui.coupon

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.demo.yourshoppingcart.common.ErrorView
import com.demo.yourshoppingcart.common.LoadingView
import com.demo.yourshoppingcart.coupon.domain.entity.couponEntity
import com.demo.yourshoppingcart.ui.cart.CartState
import com.demo.yourshoppingcart.ui.cart.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CouponsScreen(
    viewModel: CouponsViewModel,
    onBack: () -> Unit = {}
) {

    val state by viewModel.viewState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Coupons") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->

        Box(modifier = Modifier.padding(padding)) {
            when (state) {

                is CouponsState.Loading -> {
                    LoadingView()
                }

                is CouponsState.Error -> {
                    ErrorView(
                        message = (state as CouponsState.Error).error)
                }

                is CouponsState.Success -> {
                    val coupons = (state as CouponsState.Success).coupons
                    CouponsList(
                        coupons = coupons,
                        onApply = { viewModel.applyCoupon(it) },
                        onRemove = { viewModel.removeCoupon(it) }
                    )
                }
            }
        }
    }
}

@Composable
fun CouponsList(
    coupons: List<couponEntity>,
    onApply: (String) -> Unit,
    onRemove: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(coupons) { coupon ->
            CouponItem(
                coupon = coupon,
                onApply = onApply,
                onRemove = onRemove
            )
        }
    }
}

@Composable
fun CouponItem(
    coupon: couponEntity,
    onApply: (String) -> Unit,
    onRemove: (String) -> Unit
) {
    val isApplied = coupon.isApplied

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isApplied)
                MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
            else
                MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = coupon.code,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = coupon.description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Expires: ${coupon.expiryDate}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {

                if (isApplied) {
                    OutlinedButton(
                        onClick = { onRemove(coupon.id) }
                    ) {
                        Text("Remove")
                    }
                } else {
                    Button(
                        onClick = { onApply(coupon.id) }
                    ) {
                        Text("Apply")
                    }
                }
            }
        }
    }
}

