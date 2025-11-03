package com.demo.yourshoppingcart.cart.domain.repository

import com.demo.yourshoppingcart.Resource
import com.demo.yourshoppingcart.cart.data.model.CartModel
import com.demo.yourshoppingcart.cart.domain.entity.cartEntity
import com.demo.yourshoppingcart.home.data.model.HomeModel

interface CartRepository {
    suspend fun addCart(cart: CartModel.Cart): Resource<Unit>
    suspend fun getCart(cartId: String): Resource<cartEntity>
    //suspend fun getUser(): Resource<UserModel.UserResponse>
    suspend fun fetchProducts(productids: List<String>): Resource<List<HomeModel.Item>>
    suspend fun updateCart(cartId: String, cart: List<CartModel.CartItem>): Resource<String>

}