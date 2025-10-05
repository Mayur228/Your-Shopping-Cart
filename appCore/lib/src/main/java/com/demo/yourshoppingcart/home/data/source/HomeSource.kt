package com.demo.yourshoppingcart.home.data.source

import com.demo.yourshoppingcart.common.network.AppHttpClient
import com.demo.yourshoppingcart.common.network.config.ApiDomains
import com.demo.yourshoppingcart.home.data.model.HomeModel

interface HomeSource {
    suspend fun getCategory(): HomeModel.CategoryResponse
    suspend fun getAllCategoryItem(): HomeModel.CategoryItemResponse
    suspend fun getSelectedCategoryItem(catId: String): HomeModel.CategoryItemResponse
}

class HomeSourceImpl(
    private val appHttpClient: AppHttpClient,
    private val apiDomains: ApiDomains
): HomeSource{
    override suspend fun getCategory(): HomeModel.CategoryResponse {
        TODO("Not yet implemented")
    }

    override suspend fun getAllCategoryItem(): HomeModel.CategoryItemResponse {
        TODO("Not yet implemented")
    }

    override suspend fun getSelectedCategoryItem(catId: String): HomeModel.CategoryItemResponse {
        TODO("Not yet implemented")
    }

}