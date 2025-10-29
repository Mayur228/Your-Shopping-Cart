package com.demo.yourshoppingcart.user.domain.usecase

import com.demo.yourshoppingcart.Resource
import com.demo.yourshoppingcart.user.data.model.UserModel
import com.demo.yourshoppingcart.user.domain.repository.UserRepository
import org.koin.core.annotation.Factory

@Factory
class GuestLoginUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(): Resource<String> {
        return userRepository.guestLogin()
    }
}