package com.demo.yourshoppingcart.cart.data.source

import com.demo.yourshoppingcart.cart.data.model.CartModel
import com.demo.yourshoppingcart.common.network.DocumentApi
import com.demo.yourshoppingcart.home.data.model.HomeModel
import org.koin.core.annotation.Factory

interface CartSource {
    suspend fun getCart(cartId: String): CartModel.Cart
    suspend fun addCart(cart: CartModel.Cart)
    suspend fun fetchProducts(productids: List<String>): List<HomeModel.Item>
    suspend fun updateCart(cartId: String,cart: List<CartModel.CartItem>)
}

@Factory
class UserResourceImpl(
    private val documentApi: DocumentApi
) : CartSource {
    override suspend fun getCart(cartId: String): CartModel.Cart {
        return documentApi.fetchCart(cartId = cartId)
    }

    override suspend fun addCart(cart: CartModel.Cart) {
        return documentApi.addProductToCart(cart = cart)
    }

    override suspend fun fetchProducts(productids: List<String>): List<HomeModel.Item> {
        return documentApi.fetchProducts(productids)
    }

    override suspend fun updateCart(
        cartId: String,
        cart: List<CartModel.CartItem>
    ) {
        return documentApi.updateCartItem(cartId = cartId, cart = cart)
    }
}
