package com.demo.yourshoppingcart.user.domain.usecase

import com.demo.yourshoppingcart.Resource
import com.demo.yourshoppingcart.user.domain.repository.UserRepository
import org.koin.core.annotation.Factory

@Factory
class SendOtpUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(phoneNumber: String): Resource<String> {
        return userRepository.sendOtp(phoneNumber)
    }
}
