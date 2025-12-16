package com.demo.yourshoppingcart.order_details.domain.repository

import com.demo.yourshoppingcart.Resource
import com.demo.yourshoppingcart.order.domain.entity.orderEntity

interface OrderDetailsRepository {
    suspend fun getOrderDetails(id: String): Resource<orderEntity>
}