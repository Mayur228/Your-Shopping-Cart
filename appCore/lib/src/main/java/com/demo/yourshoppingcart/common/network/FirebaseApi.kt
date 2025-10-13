package com.demo.yourshoppingcart.common.network

import com.demo.yourshoppingcart.Resource
import com.demo.yourshoppingcart.home.data.model.HomeModel
import com.demo.yourshoppingcart.product_details.data.model.ProductDetailsModel
import com.demo.yourshoppingcart.user.data.model.UserModel

interface DocumentApi {
    suspend  fun fetchCategory(): HomeModel.CategoryResponse
    suspend fun fetchAllItems(): HomeModel.CategoryItemResponse
    suspend fun fetchSelectedCatItem(cat: String): HomeModel.CategoryItemResponse
    suspend fun fetchProductDetails(itemId: String): ProductDetailsModel.DetailModel
    suspend fun addProductToCart(cart: UserModel.UserCart): Unit
    suspend fun fetchCart(cartId: String): List<UserModel.UserCart>
    suspend fun getAnonymouseUser(userId: String): UserModel.UserResponse
    suspend fun addUser(isAnonymouse: Boolean,user: UserModel.UserResponse): String
}