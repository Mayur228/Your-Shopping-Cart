package com.demo.yourshoppingcart.user.domain.usecase

import com.demo.yourshoppingcart.Resource
import com.demo.yourshoppingcart.user.domain.entity.cartEntity
import com.demo.yourshoppingcart.user.domain.repository.UserRepository
import org.koin.core.annotation.Factory

@Factory
class GetCartUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(cartId: String): Resource<cartEntity> {
        return userRepository.getCart(userId = userId)
    }
}