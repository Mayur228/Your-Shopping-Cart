package com.demo.yourshoppingcart.order_details.data.model

import com.demo.yourshoppingcart.order.data.model.OrderStatus
import com.demo.yourshoppingcart.product_details.data.model.ProductDetailsModel

data class OrderDetails(
    val id: String,
    val product: ProductDetailsModel.Product,
    val paymentMethod: String,
    val productQun: Int,
    val orderDate: String,
    val orderStatus: OrderStatus
)
