package com.demo.yourshoppingcart.cart.domain.usecase

import com.demo.yourshoppingcart.Resource
import com.demo.yourshoppingcart.cart.domain.repository.CartRepository
import com.demo.yourshoppingcart.cart.domain.entity.cartEntity
import org.koin.core.annotation.Factory

@Factory
class GetCartUseCase(private val cartRepository: CartRepository) {
    suspend operator fun invoke(cartId: String): Resource<cartEntity> {
        return cartRepository.getCart(cartId = cartId)
    }
}