package com.demo.yourshoppingcart.product_details.data.repository

import com.demo.yourshoppingcart.Resource
import com.demo.yourshoppingcart.product_details.data.source.ProductDetailsSource
import com.demo.yourshoppingcart.product_details.domain.entity.detailsEntity
import com.demo.yourshoppingcart.product_details.domain.repository.ProductDetailsRepository
import org.koin.core.annotation.Factory

@Factory
class ProductDetailsRepositoryImpl(private val productDetailsSource: ProductDetailsSource): ProductDetailsRepository {
    override suspend fun getProductDetails(itemId: String): Resource<detailsEntity> {
        try {
            val data = productDetailsSource.getProductDetails(itemId = itemId)
            return Resource.Data(data)
        }catch (e: Exception) {
            return Resource.Error(e)
        }
    }
}