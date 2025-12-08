package com.demo.yourshoppingcart.order.data.model

import java.util.Date
data class OrderModel(
    val id: String,
    val status: String,            // e.g., "SHIPPING", "DELIVERED"
    val totalAmount: String,       // total amount as String (can also be Double if preferred)
    val items: List<OrderItem>,    // list of products in this order
    val paymentMethod: String,     // e.g., "COD", "UPI", "CARD"
    val orderDate: String          // order date as String (can also be Date if you want)
) {
    data class OrderItem(
        val productId: String,
        val productName: String,
        val productPrice: String,
        val productQun: Int,
        val productImg: String
    )
}


enum class OrderStatus {
    PENDING,
    SHIPING,
    OUT_FOR_DELIVERY,
    DELIVERED,
    CANCELLED
}

