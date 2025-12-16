package com.demo.yourshoppingcart.product_details.data.source

import com.demo.yourshoppingcart.common.network.AppHttpClient
import com.demo.yourshoppingcart.common.network.DocumentApi
import com.demo.yourshoppingcart.common.network.config.ApiDomains
import com.demo.yourshoppingcart.product_details.data.model.ProductDetailsModel
import org.koin.core.annotation.Factory

interface ProductDetailsSource {
    suspend fun getProductDetails(itemId: String): ProductDetailsModel.Product
}

@Factory
class ProductDetailsSourceImpl(
    private val documentApi: DocumentApi
): ProductDetailsSource {
    override suspend fun getProductDetails(itemId: String): ProductDetailsModel.Product {
       return documentApi.fetchProductDetails(itemId = itemId)
    }

}

