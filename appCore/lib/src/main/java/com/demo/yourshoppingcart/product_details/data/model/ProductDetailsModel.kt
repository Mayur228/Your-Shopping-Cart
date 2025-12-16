package com.demo.yourshoppingcart.product_details.data.model

abstract class ProductDetailsModel {

    data class Product(
        val productId: String,
        val productName: String,
        val productImg: String,
        val productDes: String,
        val productPrice: Int,
        val productDiscount: Int,
        val productRating: Double,
        val productImages: List<String>,
        val cat: String
    )
}