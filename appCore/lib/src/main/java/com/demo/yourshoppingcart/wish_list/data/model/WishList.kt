package com.demo.yourshoppingcart.wish_list.data.model

import com.demo.yourshoppingcart.product_details.data.model.ProductDetailsModel

data class WishList(
    val id: String = "",
    val product: ProductDetailsModel.Product? = null
)
