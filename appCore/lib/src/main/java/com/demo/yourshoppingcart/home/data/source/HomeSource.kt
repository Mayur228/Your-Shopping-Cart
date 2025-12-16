package com.demo.yourshoppingcart.home.data.source

import com.demo.yourshoppingcart.common.network.AppHttpClient
import com.demo.yourshoppingcart.common.network.DocumentApi
import com.demo.yourshoppingcart.common.network.config.ApiDomains
import com.demo.yourshoppingcart.home.data.model.HomeModel
import org.koin.core.annotation.Factory

interface HomeSource {
    suspend fun getCategory(): List<HomeModel.Category>
    suspend fun getAllCategoryItem(): List<HomeModel.Product>
    suspend fun getSelectedCategoryItem(cat: String): List<HomeModel.Product>
}

@Factory
class HomeSourceImpl(
    private val documentApi: DocumentApi
): HomeSource {
    override suspend fun getCategory(): List<HomeModel.Category> {
        return documentApi.fetchCategory()
    }

    override suspend fun getAllCategoryItem(): List<HomeModel.Product> {
        return documentApi.fetchAllItems()
    }

    override suspend fun getSelectedCategoryItem(cat: String): List<HomeModel.Product> {
        return documentApi.fetchSelectedCatProduct(cat = cat)
    }

}