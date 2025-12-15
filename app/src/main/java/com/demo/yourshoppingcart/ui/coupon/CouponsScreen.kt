package com.demo.yourshoppingcart.ui.coupon

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.demo.yourshoppingcart.common.ErrorView
import com.demo.yourshoppingcart.common.LoadingView
import com.demo.yourshoppingcart.coupon.domain.entity.couponEntity
import com.demo.yourshoppingcart.ui.theme.YourShoppingCartTheme

@Composable
fun CouponsScreen(
    viewModel: CouponsViewModel,
    onBack: () -> Unit = {}
) {
    val state by viewModel.viewState.collectAsState()

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 12.dp)) {

        when (state) {
            is CouponsState.Loading -> LoadingView()

            is CouponsState.Error ->
                ErrorView((state as CouponsState.Error).error)

            is CouponsState.Success -> {
                CouponsList(
                    coupons = (state as CouponsState.Success).coupons,
                    onApply = { viewModel.applyCoupon(it) },
                    onRemove = { viewModel.removeCoupon(it) }
                )
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
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier.fillMaxSize()
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
    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val isApplied = coupon.isApplied

    val primary = MaterialTheme.colorScheme.primary
    val primarySoft = primary.copy(alpha = if (isDark) 0.25f else 0.18f)

    val cardBg = MaterialTheme.colorScheme.surface
    val textGray = MaterialTheme.colorScheme.onSurfaceVariant
    val lineColor = MaterialTheme.colorScheme.outline.copy(alpha = if (isDark) 0.45f else 0.35f)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = if (isDark) 0.4f else 0.6f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // ---------------------------------------------------------------
            // HEADER (Coupon)
            // ---------------------------------------------------------------
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Coupon",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = primary
                    )
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(primarySoft)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        "Valid Until ${coupon.expiryDate}",
                        style = MaterialTheme.typography.labelMedium.copy(color = primary)
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            // ---------------------------------------------------------------
            // DOTTED LINE
            // ---------------------------------------------------------------
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
            ) {
                val dashWidth = 12f
                val dashGap = 8f
                var x = 0f

                while (x < size.width) {
                    drawLine(
                        color = lineColor,
                        start = Offset(x, 0f),
                        end = Offset(x + dashWidth, 0f),
                        strokeWidth = 3f
                    )
                    x += dashWidth + dashGap
                }
            }

            Spacer(Modifier.height(12.dp))

            // ---------------------------------------------------------------
            // MAIN CONTENT
            // ---------------------------------------------------------------
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(primarySoft),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CardGiftcard,
                            contentDescription = null,
                            tint = primary
                        )
                    }

                    Spacer(Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {

                        Text(
                            text = coupon.code,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(Modifier.height(2.dp))

                        Text(
                            text = coupon.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = textGray,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Spacer(Modifier.width(12.dp))

                // ---------------------------------------------------------------
                // BUTTONS (Apply / Applied + Remove)
                // ---------------------------------------------------------------
                if (isApplied) {

                    Column(horizontalAlignment = Alignment.End) {

                        // Applied badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50))
                                .background(primary.copy(alpha = 0.15f))
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(
                                "Applied",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = primary
                                )
                            )
                        }

                        Spacer(Modifier.height(6.dp))

                        // Remove button
                        Button(
                            onClick = { onRemove(coupon.id) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error
                            ),
                            shape = RoundedCornerShape(50),
                            modifier = Modifier.height(32.dp)
                        ) {
                            Text("Remove")
                        }
                    }

                } else {

                    Button(
                        onClick = { onApply(coupon.id) },
                        colors = ButtonDefaults.buttonColors(primary),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.height(38.dp)
                    ) {
                        Text("Apply")
                    }
                }
            }
        }
    }
}

