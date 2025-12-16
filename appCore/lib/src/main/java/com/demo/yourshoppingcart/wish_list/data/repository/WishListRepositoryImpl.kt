package com.demo.yourshoppingcart.wish_list.data.repository

import com.demo.yourshoppingcart.Resource
import com.demo.yourshoppingcart.wish_list.data.model.WishList
import com.demo.yourshoppingcart.wish_list.data.source.WishListSource
import com.demo.yourshoppingcart.wish_list.domain.repository.WishListRepository
import org.koin.core.annotation.Factory

@Factory
class WishListRepositoryImpl(private val source: WishListSource): WishListRepository {
    override suspend fun getWishList(): Resource<List<WishList>> {
        return source.getWishList()
    }

    override suspend fun addToWishList(wishList: WishList): Resource<Unit> {
        return source.addToWishList(wishList = wishList)
    }

    override suspend fun removeWishList(id: String): Resource<Unit> {
        return source.removeWishList(id = id)
    }
}