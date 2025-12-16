package com.demo.yourshoppingcart.wish_list.domain.usecase

import com.demo.yourshoppingcart.Resource
import com.demo.yourshoppingcart.wish_list.data.model.WishList
import com.demo.yourshoppingcart.wish_list.domain.repository.WishListRepository
import org.koin.core.annotation.Factory

@Factory
class RemoveWishListUseCase(private val repository: WishListRepository) {
    suspend operator fun invoke(id: String): Resource<Unit> {
        return repository.removeWishList(id = id)
    }
}