package com.demo.yourshoppingcart.user.data.source

import com.demo.yourshoppingcart.common.network.DocumentApi
import com.demo.yourshoppingcart.user.data.model.UserModel
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Factory

interface UserSource {
    suspend fun getCart(cartId: String): UserModel.UserCart
    suspend fun addCart(cart: UserModel.UserCart)
    suspend fun getUser(): UserModel.UserResponse
    suspend fun guestLogin(): String
    suspend fun phoneLogin(
        oldGuestId: String? = null,
        user: UserModel.UserResponse,
        verificationId: String? = null,
        otp: String? = null
    ): String
    suspend fun sendOtp(phoneNumber: String): String


}

@Factory
class UserResourceImpl(
    private val documentApi: DocumentApi
) : UserSource {
    override suspend fun getCart(cartId: String): UserModel.UserCart {
        return documentApi.fetchCart(cartId = cartId)
    }

    override suspend fun addCart(cart: UserModel.UserCart) {
        return documentApi.addProductToCart(cart = cart)
    }

    override suspend fun getUser(): UserModel.UserResponse {
        return documentApi.fetchUser()
    }

    override suspend fun guestLogin(): String {
        return documentApi.guestLogin()
    }

    override suspend fun phoneLogin(
        oldGuestId: String?,
        user: UserModel.UserResponse,
        verificationId: String?,
        otp: String?
    ): String {
        return documentApi.phoneLogin(oldGuestId = oldGuestId,user = user, verificationId = verificationId, otp = otp)
    }

    override suspend fun sendOtp(phoneNumber: String): String {
        return documentApi.sendOtpFlow(phoneNumber = phoneNumber)
    }


}
