package com.demo.yourshoppingcart.user.data.repository

import com.demo.yourshoppingcart.Resource
import com.demo.yourshoppingcart.user.data.model.UserModel
import com.demo.yourshoppingcart.user.data.source.UserSource
import com.demo.yourshoppingcart.user.domain.entity.userResponseEntity
import com.demo.yourshoppingcart.user.domain.repository.UserRepository
import org.koin.core.annotation.Factory

@Factory
class UserRepositoryImpl(private val source: UserSource): UserRepository {
    override suspend fun addCart(entity: List<UserModel.UserCart>): Resource<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun getCart(userId: String): Resource<userResponseEntity> {
        TODO("Not yet implemented")
    }
}