package com.demo.yourshoppingcart.home.domain.usecase

import com.demo.yourshoppingcart.Resource
import com.demo.yourshoppingcart.user.data.model.UserModel
import com.demo.yourshoppingcart.user.domain.repository.UserRepository
import org.koin.core.annotation.Factory

@Factory
class GetQuantityUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(): Resource<UserModel.UserResponse> {
        return userRepository.getUser()
    }
}