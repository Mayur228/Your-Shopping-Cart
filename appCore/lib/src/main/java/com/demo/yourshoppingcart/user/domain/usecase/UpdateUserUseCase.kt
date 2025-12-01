package com.demo.yourshoppingcart.user.domain.usecase

import com.demo.yourshoppingcart.Resource
import com.demo.yourshoppingcart.user.data.model.AddressModel
import com.demo.yourshoppingcart.user.data.model.UserModel
import com.demo.yourshoppingcart.user.domain.repository.UserRepository
import org.koin.core.annotation.Factory

@Factory
class UpdateUserUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(id: String,user: UserModel.UserResponse): Resource<String> {
        return userRepository.updateUser(id = id,user = user)
    }
}