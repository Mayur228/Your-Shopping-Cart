package com.demo.yourshoppingcart.order.domain.usecase

import com.demo.yourshoppingcart.Resource
import com.demo.yourshoppingcart.order.data.model.OrderModel
import com.demo.yourshoppingcart.order.domain.reposioty.OrderRepository
import com.demo.yourshoppingcart.user.data.model.AddressModel
import com.demo.yourshoppingcart.user.data.model.UserModel
import com.demo.yourshoppingcart.user.domain.repository.UserRepository
import org.koin.core.annotation.Factory

@Factory
class GetOrderHistoryUseCase(private val orderRepository: OrderRepository) {
    suspend operator fun invoke(): Resource<List<OrderModel>> {
        return orderRepository.getOrderHistory()
    }
}