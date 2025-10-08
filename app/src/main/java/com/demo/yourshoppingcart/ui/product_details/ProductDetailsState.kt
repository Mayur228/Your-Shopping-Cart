package com.demo.yourshoppingcart.ui.product_details

import com.demo.yourshoppingcart.product_details.domain.entity.detailsEntity

data class ProductDetailsState(
    val isLoading: Boolean = false,
    val item: detailsEntity? = null,
    val errorMessage: String? = null
)
