package com.demo.yourshoppingcart.wish_list.data.source

import com.demo.yourshoppingcart.Resource
import com.demo.yourshoppingcart.common.network.DocumentApi
import com.demo.yourshoppingcart.wish_list.data.model.WishList
import org.koin.core.annotation.Factory

interface WishListSource {
    suspend fun getWishList(): Resource<List<WishList>>
    suspend fun addToWishList(wishList: WishList): Resource<Unit>
    suspend fun removeWishList(id: String): Resource<Unit>
}

@Factory
class WishListSourceImpl(private val api: DocumentApi): WishListSource {
    override suspend fun getWishList(): Resource<List<WishList>> {
       return try {
            val data = api.getWishList()
            Resource.Data(data)
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    override suspend fun addToWishList(wishList: WishList): Resource<Unit> {
        return try {
            val data = api.addWishList(product =wishList)
            Resource.Data(data)
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    override suspend fun removeWishList(id: String): Resource<Unit> {
        return try {
            val data = api.removeWishList(id = id)
            Resource.Data(data)
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

}