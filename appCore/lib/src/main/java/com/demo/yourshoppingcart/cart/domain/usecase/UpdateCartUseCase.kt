package com.demo.yourshoppingcart.cart.domain.usecase

import com.demo.yourshoppingcart.Resource
import com.demo.yourshoppingcart.cart.data.model.CartModel
import com.demo.yourshoppingcart.cart.domain.repository.CartRepository
import com.demo.yourshoppingcart.user.data.model.UserModel
import com.demo.yourshoppingcart.user.domain.repository.UserRepository
import org.koin.core.annotation.Factory

@Factory
class UpdateCartUseCase(private val cartRepository: CartRepository) {
    suspend operator fun invoke(cartId: String, cart: List<CartModel.CartItem>, ): Resource<String> {
        return cartRepository.updateCart(cartId = cartId, cart = cart)
    }
}