package com.demo.yourshoppingcart.product_details.domain.usecase

import com.demo.yourshoppingcart.Resource
import com.demo.yourshoppingcart.product_details.domain.entity.detailsEntity
import com.demo.yourshoppingcart.product_details.domain.repository.ProductDetailsRepository
import org.koin.core.annotation.Factory

@Factory
class GetProductDetailsUseCase(private val productDetailsRepository: ProductDetailsRepository) {
    suspend operator fun invoke(itemId: String): Resource<detailsEntity> {
        return productDetailsRepository.getProductDetails(itemId = itemId)
    }
}