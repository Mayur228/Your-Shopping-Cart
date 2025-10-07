package com.demo.yourshoppingcart.home.data.source

import com.demo.yourshoppingcart.common.network.AppHttpClient
import com.demo.yourshoppingcart.common.network.DocumentApi
import com.demo.yourshoppingcart.common.network.config.ApiDomains
import com.demo.yourshoppingcart.home.data.model.HomeModel
import org.koin.core.annotation.Factory

interface HomeSource {
    suspend fun getCategory(): HomeModel.CategoryResponse
    suspend fun getAllCategoryItem(): HomeModel.CategoryItemResponse
    suspend fun getSelectedCategoryItem(cat: String): HomeModel.CategoryItemResponse
}

@Factory
class HomeSourceImpl(
    private val appHttpClient: AppHttpClient,
    private val apiDomains: ApiDomains,
    private val documentApi: DocumentApi
): HomeSource {
    override suspend fun getCategory(): HomeModel.CategoryResponse {
        return documentApi.fetchCategory()
    }

    override suspend fun getAllCategoryItem(): HomeModel.CategoryItemResponse {
        return documentApi.fetchAllItems()
    }

    override suspend fun getSelectedCategoryItem(cat: String): HomeModel.CategoryItemResponse {
        return documentApi.fetchSelectedCatItem(cat = cat)
    }

}