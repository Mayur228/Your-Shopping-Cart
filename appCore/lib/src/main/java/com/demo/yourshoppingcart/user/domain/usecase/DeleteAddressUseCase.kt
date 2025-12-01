package com.demo.yourshoppingcart.user.domain.usecase

import com.demo.yourshoppingcart.Resource
import com.demo.yourshoppingcart.user.data.model.AddressModel
import com.demo.yourshoppingcart.user.data.model.UserModel
import com.demo.yourshoppingcart.user.domain.repository.UserRepository
import org.koin.core.annotation.Factory

@Factory
class DeleteAddressUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(id: String): Resource<String> {
        return userRepository.deletedAddress(id = id)
    }
}