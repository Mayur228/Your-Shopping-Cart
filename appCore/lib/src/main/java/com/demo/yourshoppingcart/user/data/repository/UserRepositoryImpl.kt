package com.demo.yourshoppingcart.user.data.repository

import com.demo.yourshoppingcart.Resource
import com.demo.yourshoppingcart.user.data.model.UserModel
import com.demo.yourshoppingcart.user.data.source.UserSource
import com.demo.yourshoppingcart.user.domain.entity.cartEntity
import com.demo.yourshoppingcart.user.domain.repository.UserRepository
import org.koin.core.annotation.Factory

@Factory
class UserRepositoryImpl(private val source: UserSource) : UserRepository {
    override suspend fun addCart(entity: List<UserModel.UserCart>): Resource<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun getCart(cartId: String): Resource<cartEntity> {
        return try {
            val data = source.getCart(cartId = cartId)
            Resource.Data(data)
        }catch (e: Exception) {
            Resource.Error(e)
        }
    }

    override suspend fun getUser(): Resource<UserModel.UserResponse> {
        return try {
            val data = source.getUser()
            Resource.Data(data)
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }
}