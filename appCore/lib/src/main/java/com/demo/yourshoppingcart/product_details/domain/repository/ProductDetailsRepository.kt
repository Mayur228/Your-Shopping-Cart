package com.demo.yourshoppingcart.product_details.domain.repository

import com.demo.yourshoppingcart.Resource
import com.demo.yourshoppingcart.product_details.domain.entity.detailsEntity

interface ProductDetailsRepository {
    suspend fun getProductDetails(itemId: String): Resource<detailsEntity>
}