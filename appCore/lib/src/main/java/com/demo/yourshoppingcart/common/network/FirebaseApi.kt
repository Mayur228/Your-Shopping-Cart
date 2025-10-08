package com.demo.yourshoppingcart.common.network

import com.demo.yourshoppingcart.Resource
import com.demo.yourshoppingcart.home.data.model.HomeModel
import com.demo.yourshoppingcart.product_details.data.model.ProductDetailsModel

interface DocumentApi {
    suspend  fun fetchCategory(): HomeModel.CategoryResponse
    suspend fun fetchAllItems(): HomeModel.CategoryItemResponse
    suspend fun fetchSelectedCatItem(cat: String): HomeModel.CategoryItemResponse
    suspend fun fetchSingleData(): Map<String, String>
    suspend fun fetchProductDetails(itemId: String): ProductDetailsModel.DetailModel
}