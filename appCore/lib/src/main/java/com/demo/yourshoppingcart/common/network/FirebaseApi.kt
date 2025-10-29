package com.demo.yourshoppingcart.common.network

import com.demo.yourshoppingcart.home.data.model.HomeModel
import com.demo.yourshoppingcart.product_details.data.model.ProductDetailsModel
import com.demo.yourshoppingcart.user.data.model.UserModel
import kotlinx.coroutines.flow.Flow

interface DocumentApi {
    suspend fun fetchCategory(): HomeModel.CategoryResponse
    suspend fun fetchAllItems(): HomeModel.CategoryItemResponse
    suspend fun fetchSelectedCatItem(cat: String): HomeModel.CategoryItemResponse
    suspend fun fetchProductDetails(itemId: String): ProductDetailsModel.DetailModel
    suspend fun addProductToCart(cart: UserModel.UserCart)
    suspend fun fetchCart(cartId: String): UserModel.UserCart
    suspend fun clearCart()
    suspend fun fetchUser(): UserModel.UserResponse
    suspend fun guestLogin(): String
    suspend fun phoneLogin(oldGuestId: String? = null, user: UserModel.UserResponse, verificationId: String? = null, otp: String? = null): String
    suspend fun sendOtpFlow(phoneNumber: String): String

}