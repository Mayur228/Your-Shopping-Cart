package com.demo.yourshoppingcart.user.domain.usecase

import com.demo.yourshoppingcart.Resource
import com.demo.yourshoppingcart.user.data.model.UserModel
import com.demo.yourshoppingcart.user.domain.repository.UserRepository
import org.koin.core.annotation.Factory

@Factory
class PhoneLoginUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(oldGuestId: String? = null,user: UserModel.UserResponse, verificationId: String? = null, otp: String? = null): Resource<String> {
        return userRepository.phoneLogin(
            oldGuestId = oldGuestId,
            user = user,
            verificationId = verificationId,
            otp = otp,
        )
    }
}