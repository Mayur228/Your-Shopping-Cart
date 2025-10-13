package com.demo.yourshoppingcart.user.domain.usecase

import com.demo.yourshoppingcart.Resource
import com.demo.yourshoppingcart.user.data.model.UserModel
import com.demo.yourshoppingcart.user.domain.repository.UserRepository
import org.koin.core.annotation.Factory

@Factory
class AddCartUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(cartEntity: List<UserModel.UserCart>): Resource<Unit> {
        return userRepository.addCart(cartEntity)
    }
}