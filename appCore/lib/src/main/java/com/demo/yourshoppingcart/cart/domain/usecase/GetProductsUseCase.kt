package com.demo.yourshoppingcart.cart.domain.usecase

import com.demo.yourshoppingcart.Resource
import com.demo.yourshoppingcart.cart.data.model.CartModel
import com.demo.yourshoppingcart.cart.domain.repository.CartRepository
import com.demo.yourshoppingcart.home.data.model.HomeModel
import com.demo.yourshoppingcart.user.domain.repository.UserRepository
import org.koin.core.annotation.Factory

@Factory
class GetProductsUseCase(private val cartRepository: CartRepository) {
    suspend operator fun invoke(productIds: List<String>): Resource<List<HomeModel.Item>> {
        return cartRepository.fetchProducts(productIds)
    }
}