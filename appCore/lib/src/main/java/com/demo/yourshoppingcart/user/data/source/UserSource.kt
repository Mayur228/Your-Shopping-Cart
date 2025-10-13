package com.demo.yourshoppingcart.user.data.source

import com.demo.yourshoppingcart.common.network.AppHttpClient
import com.demo.yourshoppingcart.common.network.DocumentApi
import com.demo.yourshoppingcart.common.network.config.ApiDomains
import com.demo.yourshoppingcart.user.data.model.UserModel
import org.koin.core.annotation.Factory

interface UserSource {
    suspend fun getCart(cartId: String): UserModel.UserCart
    suspend fun addCart(cart: UserModel.UserCart): Unit
    suspend fun getUser(): UserModel.UserResponse
}

@Factory
class UserResourceImpl(
    private val appHttpClient: AppHttpClient,
    private val apiDomains: ApiDomains,
    private val documentApi: DocumentApi
): UserSource {
    override suspend fun getCart(cartId: String): UserModel.UserCart {
        return documentApi.fetchCart(cartId = cartId)
    }

    override suspend fun addCart(cart: UserModel.UserCart) {
        TODO("Not yet implemented")
    }

    override suspend fun getUser(): UserModel.UserResponse {
        return documentApi.getAnonymouseUser()
    }

}
