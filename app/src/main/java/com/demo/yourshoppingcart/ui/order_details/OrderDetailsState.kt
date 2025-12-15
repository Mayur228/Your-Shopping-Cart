package com.demo.yourshoppingcart.ui.order_details

import com.demo.yourshoppingcart.order.domain.entity.orderEntity

sealed class OrderDetailsState {
    object Loading: OrderDetailsState()
    data class Success(val order: orderEntity): OrderDetailsState()
    data class Error(val error: String): OrderDetailsState()
}