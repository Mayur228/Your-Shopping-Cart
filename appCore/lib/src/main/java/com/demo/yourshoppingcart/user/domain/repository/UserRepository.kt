package com.demo.yourshoppingcart.user.domain.repository

import com.demo.yourshoppingcart.Resource
import com.demo.yourshoppingcart.home.data.model.HomeModel
import com.demo.yourshoppingcart.user.data.model.AddressModel
import com.demo.yourshoppingcart.user.data.model.UserModel
import com.demo.yourshoppingcart.user.domain.entity.cartEntity

interface UserRepository {
    suspend fun isNewUser(): Resource<Boolean>
    suspend fun getUser(): Resource<UserModel.UserResponse>
    suspend fun updateUser(id: String,user: UserModel.UserResponse): Resource<String>
    suspend fun guestLogin(): Resource<String>
    suspend fun phoneLogin(oldGuestId: String? = null,user: UserModel.UserResponse, verificationId: String? = null, otp: String? = null): Resource<String>
    suspend fun sendOtp(phoneNumber: String): Resource<String>
    suspend fun addAddress(address: AddressModel): Resource<String>
    suspend fun updateAddress(id: String,address: AddressModel): Resource<String>
    suspend fun deletedAddress(id: String): Resource<String>
}