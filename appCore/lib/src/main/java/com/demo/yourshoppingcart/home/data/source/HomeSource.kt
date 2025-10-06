package com.demo.yourshoppingcart.home.data.source

import com.demo.yourshoppingcart.common.network.AppHttpClient
import com.demo.yourshoppingcart.common.network.DocumentApi
import com.demo.yourshoppingcart.common.network.config.ApiDomains
import com.demo.yourshoppingcart.home.data.model.HomeModel
import org.koin.core.annotation.Factory

interface HomeSource {
    suspend fun getCategory(): HomeModel.CategoryResponse
    suspend fun getAllCategoryItem(): HomeModel.CategoryItemResponse
    suspend fun getSelectedCategoryItem(catId: String): HomeModel.CategoryItemResponse
}