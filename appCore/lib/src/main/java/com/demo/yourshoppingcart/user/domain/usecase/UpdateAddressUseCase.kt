package com.demo.yourshoppingcart.user.domain.usecase

import com.demo.yourshoppingcart.Resource
import com.demo.yourshoppingcart.user.data.model.AddressModel
import com.demo.yourshoppingcart.user.data.model.UserModel
import com.demo.yourshoppingcart.user.domain.repository.UserRepository
import org.koin.core.annotation.Factory

@Factory
class UpdateAddressUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(id: String,address: AddressModel): Resource<String> {
        return userRepository.updateAddress(id = id,address = address)
    }
}