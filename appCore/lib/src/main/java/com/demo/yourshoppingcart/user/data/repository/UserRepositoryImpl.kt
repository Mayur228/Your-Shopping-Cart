package com.demo.yourshoppingcart.user.data.repository

import com.demo.yourshoppingcart.Resource
import com.demo.yourshoppingcart.home.data.model.HomeModel
import com.demo.yourshoppingcart.user.data.model.AddressModel
import com.demo.yourshoppingcart.user.data.model.UserModel
import com.demo.yourshoppingcart.user.data.source.UserSource
import com.demo.yourshoppingcart.user.domain.entity.cartEntity
import com.demo.yourshoppingcart.user.domain.repository.UserRepository
import org.koin.core.annotation.Factory

@Factory
class UserRepositoryImpl(private val source: UserSource) : UserRepository {

    override suspend fun isNewUser(): Resource<Boolean> {
        return try {
            val data = source.isNewUser()
            Resource.Data(data)
        } catch (e: Exception) {
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

    override suspend fun updateUser(id: String, user: UserModel.UserResponse): Resource<String> {
        return try {
            val data = source.updateUser(id = id, user = user)
            Resource.Data("User Data Updated")
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    override suspend fun guestLogin(): Resource<String> {
        return try {
            val data = source.guestLogin()
            Resource.Data(data)
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    override suspend fun phoneLogin(
        oldGuestId: String?,
        user: UserModel.UserResponse,
        verificationId: String?,
        otp: String?
    ): Resource<String> {
        return try {
            val data = source.phoneLogin(
                oldGuestId = oldGuestId,
                user = user,
                verificationId = verificationId,
                otp = otp
            )
            Resource.Data(data)
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    override suspend fun sendOtp(phoneNumber: String): Resource<String> {
        return try {
            val data = source.sendOtp(phoneNumber)
            Resource.Data(data)
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    override suspend fun addAddress(address: AddressModel): Resource<String> {
        return try {
            source.addAddress(address = address)
            Resource.Data("Address Added")
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    override suspend fun updateAddress(
        id: String,
        address: AddressModel
    ): Resource<String> {
        return try {
            source.updateAddress(id = id,address = address)
            Resource.Data("Address Updated")
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    override suspend fun deletedAddress(id: String): Resource<String> {
        return try {
            source.deletedAddress(id = id)
            Resource.Data("Address Deleted")
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }
}