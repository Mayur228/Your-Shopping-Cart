package com.demo.yourshoppingcart.cart.data.source

import com.demo.yourshoppingcart.cart.data.model.CartModel
import com.demo.yourshoppingcart.common.network.DocumentApi
import com.demo.yourshoppingcart.home.data.model.HomeModel
import com.demo.yourshoppingcart.product_details.data.model.ProductDetailsModel
import org.koin.core.annotation.Factory

interface CartSource {
    suspend fun getCart(): CartModel.Cart
    suspend fun addCart(cart: CartModel.Cart)
    suspend fun fetchProducts(productId: String): ProductDetailsModel.Product
    suspend fun updateCart(cartId: String,cart: List<CartModel.CartItem>)
    suspend fun clearCart()
}

@Factory
class CartResourceImpl(
    private val documentApi: DocumentApi
) : CartSource {
    override suspend fun getCart(): CartModel.Cart {
        return documentApi.fetchCart()
    }

    override suspend fun addCart(cart: CartModel.Cart) {
        return documentApi.addProductToCart(cart = cart)
    }

    override suspend fun fetchProducts(productId: String): ProductDetailsModel.Product {
        return documentApi.fetchProductDetails(productId)
    }

    override suspend fun updateCart(
        cartId: String,
        cart: List<CartModel.CartItem>
    ) {
        return documentApi.updateCartItem(cartId = cartId, cart = cart)
    }

    override suspend fun clearCart() {
        return documentApi.clearCart()
    }
}
