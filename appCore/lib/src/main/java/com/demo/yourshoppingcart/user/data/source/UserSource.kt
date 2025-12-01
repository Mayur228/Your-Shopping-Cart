package com.demo.yourshoppingcart.user.data.source

import com.demo.yourshoppingcart.common.network.DocumentApi
import com.demo.yourshoppingcart.home.data.model.HomeModel
import com.demo.yourshoppingcart.user.data.model.AddressModel
import com.demo.yourshoppingcart.user.data.model.UserModel
import org.koin.core.annotation.Factory

interface UserSource {
    suspend fun isNewUser(): Boolean
    suspend fun getUser(): UserModel.UserResponse
    suspend fun updateUser(id: String,user: UserModel.UserResponse)
    suspend fun guestLogin(): String
    suspend fun phoneLogin(
        oldGuestId: String? = null,
        user: UserModel.UserResponse,
        verificationId: String? = null,
        otp: String? = null
    ): String
    suspend fun sendOtp(phoneNumber: String): String
    suspend fun addAddress(address: AddressModel)
    suspend fun updateAddress(id: String,address: AddressModel)
    suspend fun deletedAddress(id: String)
}

@Factory
class UserResourceImpl(
    private val documentApi: DocumentApi
) : UserSource {

    override suspend fun isNewUser(): Boolean {
        return documentApi.isNewUser()
    }
    override suspend fun getUser(): UserModel.UserResponse {
        return documentApi.fetchUser()
    }

    override suspend fun updateUser(id: String, user: UserModel.UserResponse) {
        return documentApi.updateUser(userId = id, user = user)
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

    override suspend fun addAddress(address: AddressModel) {
        return documentApi.addAddress(address = address)
    }

    override suspend fun updateAddress(id: String,address: AddressModel) {
        return documentApi.updateAddress(id = id, address = address)
    }

    override suspend fun deletedAddress(id: String) {
        return documentApi.deleteAddress(id = id)
    }
}
