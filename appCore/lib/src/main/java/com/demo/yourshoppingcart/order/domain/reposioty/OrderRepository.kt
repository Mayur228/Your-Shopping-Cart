package com.demo.yourshoppingcart.order.domain.reposioty

import com.demo.yourshoppingcart.Resource
import com.demo.yourshoppingcart.order.data.model.OrderModel

interface OrderRepository {
    suspend fun addOrderHistory(order: OrderModel): Resource<Unit>
    suspend fun getOrderHistory(): Resource<List<OrderModel>>
    suspend fun getOrderDetails(id: String): Resource<OrderModel>
}