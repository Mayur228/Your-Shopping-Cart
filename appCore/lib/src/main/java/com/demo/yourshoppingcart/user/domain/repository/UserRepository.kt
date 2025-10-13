package com.demo.yourshoppingcart.user.domain.repository

import com.demo.yourshoppingcart.Resource
import com.demo.yourshoppingcart.user.data.model.UserModel
import com.demo.yourshoppingcart.user.domain.entity.cartEntity
import com.demo.yourshoppingcart.user.domain.entity.userResponseEntity

interface UserRepository {
    suspend fun addCart(entity: List<UserModel.UserCart>): Resource<Unit>
    suspend fun getCart(userId: String): Resource<cartEntity>
}