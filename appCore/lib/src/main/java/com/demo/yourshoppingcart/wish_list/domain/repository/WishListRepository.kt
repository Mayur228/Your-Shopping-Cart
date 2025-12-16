package com.demo.yourshoppingcart.wish_list.domain.repository

import com.demo.yourshoppingcart.Resource
import com.demo.yourshoppingcart.wish_list.data.model.WishList

interface WishListRepository {
    suspend fun getWishList(): Resource<List<WishList>>
    suspend fun addToWishList(wishList: WishList): Resource<Unit>
    suspend fun removeWishList(id: String): Resource<Unit>
}