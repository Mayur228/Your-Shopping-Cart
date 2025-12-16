package com.demo.yourshoppingcart.order.data.repository

import com.demo.yourshoppingcart.Resource
import com.demo.yourshoppingcart.order.data.model.OrderModel
import com.demo.yourshoppingcart.order.data.source.OrderSource
import com.demo.yourshoppingcart.order.domain.reposioty.OrderRepository
import org.koin.core.annotation.Factory

@Factory
class OrderRepositoryImpl(private val source: OrderSource): OrderRepository {
    override suspend fun addOrderHistory(order: OrderModel): Resource<Unit> {
        return source.addOrderHistory(order = order)
    }

    override suspend fun getOrderHistory(): Resource<List<OrderModel>> {
        return source.getOrderHistory()
    }
}