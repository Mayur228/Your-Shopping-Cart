package com.demo.yourshoppingcart.user.domain.repository

import com.demo.yourshoppingcart.Resource
import com.demo.yourshoppingcart.user.data.model.UserModel
import com.demo.yourshoppingcart.user.domain.entity.cartEntity

interface UserRepository {
    suspend fun addCart(cart: UserModel.UserCart): Resource<Unit>
    suspend fun getCart(cartId: String): Resource<cartEntity>
    suspend fun getUser(): Resource<UserModel.UserResponse>
    suspend fun guestLogin(): Resource<String>
    suspend fun phoneLogin(oldGuestId: String? = null,user: UserModel.UserResponse, verificationId: String? = null, otp: String? = null): Resource<String>
    suspend fun sendOtp(phoneNumber: String): String

}