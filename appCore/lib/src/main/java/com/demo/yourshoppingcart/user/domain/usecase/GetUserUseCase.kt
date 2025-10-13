package com.demo.yourshoppingcart.user.domain.usecase

import com.demo.yourshoppingcart.Resource
import com.demo.yourshoppingcart.user.data.model.UserModel
import com.demo.yourshoppingcart.user.domain.repository.UserRepository
import org.koin.core.annotation.Factory

@Factory
class GetUserUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(): Resource<UserModel.UserResponse> {
        return userRepository.getUser()
    }
}