package com.demo.yourshoppingcart.ui.product_details

import com.demo.yourshoppingcart.product_details.domain.entity.detailsEntity

sealed class ProductDetailsState {
    object Loading: ProductDetailsState()
    data class Success(val product: detailsEntity): ProductDetailsState()
    data class Error(val error: String): ProductDetailsState()
}
