package com.demo.yourshoppingcart.order_details.data.repository

import com.demo.yourshoppingcart.Resource
import com.demo.yourshoppingcart.order.domain.entity.orderEntity
import com.demo.yourshoppingcart.order_details.data.source.OrderDetails
import com.demo.yourshoppingcart.order_details.domain.repository.OrderDetailsRepository
import org.koin.core.annotation.Factory

@Factory
class OrderDetailsRepositoryImpl(private val source: OrderDetails): OrderDetailsRepository {
    override suspend fun getOrderDetails(id: String): Resource<orderEntity> {
        return source.getOrderDetails(id = id)
    }
}