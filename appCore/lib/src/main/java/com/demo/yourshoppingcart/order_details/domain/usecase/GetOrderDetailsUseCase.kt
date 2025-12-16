package com.demo.yourshoppingcart.order_details.domain.usecase

import com.demo.yourshoppingcart.Resource
import com.demo.yourshoppingcart.order.data.model.OrderModel
import com.demo.yourshoppingcart.order.domain.reposioty.OrderRepository
import com.demo.yourshoppingcart.order_details.domain.repository.OrderDetailsRepository
import org.koin.core.annotation.Factory

@Factory
class GetOrderDetailsUseCase(private val orderDetailsRepository: OrderDetailsRepository) {
    suspend operator fun invoke(id: String): Resource<OrderModel> {
        return orderDetailsRepository.getOrderDetails(id = id)
    }
}