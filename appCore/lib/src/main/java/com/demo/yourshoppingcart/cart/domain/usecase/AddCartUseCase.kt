package com.demo.yourshoppingcart.cart.domain.usecase

import com.demo.yourshoppingcart.Resource
import com.demo.yourshoppingcart.cart.data.model.CartModel
import com.demo.yourshoppingcart.cart.domain.repository.CartRepository
import com.demo.yourshoppingcart.user.data.model.UserModel
import com.demo.yourshoppingcart.user.domain.repository.UserRepository
import org.koin.core.annotation.Factory

@Factory
class AddCartUseCase(private val cartRepository: CartRepository) {
    suspend operator fun invoke(cart: CartModel.Cart): Resource<Unit> {
        return cartRepository.addCart(cart = cart)
    }
}