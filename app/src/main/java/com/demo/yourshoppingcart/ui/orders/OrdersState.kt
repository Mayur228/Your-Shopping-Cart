package com.demo.yourshoppingcart.ui.orders

import com.demo.yourshoppingcart.order.domain.entity.orderEntity

sealed class OrdersState {
    object Loading: OrdersState()
    data class Success(val orderList: List<orderEntity>): OrdersState()
    data class Error(val error: String): OrdersState()
}