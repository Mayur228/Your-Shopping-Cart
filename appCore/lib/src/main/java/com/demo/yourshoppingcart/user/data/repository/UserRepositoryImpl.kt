package com.demo.yourshoppingcart.user.data.repository

import com.demo.yourshoppingcart.Resource
import com.demo.yourshoppingcart.home.data.model.HomeModel
import com.demo.yourshoppingcart.user.data.model.UserModel
import com.demo.yourshoppingcart.user.data.source.UserSource
import com.demo.yourshoppingcart.user.domain.entity.cartEntity
import com.demo.yourshoppingcart.user.domain.repository.UserRepository
import org.koin.core.annotation.Factory

@Factory
class UserRepositoryImpl(private val source: UserSource) : UserRepository {

    override suspend fun getUser(): Resource<UserModel.UserResponse> {
        return try {
            val data = source.getUser()
            Resource.Data(data)
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

    override suspend fun sendOtp(phoneNumber: String): String {
        return source.sendOtp(phoneNumber)
    }
}