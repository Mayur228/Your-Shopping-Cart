package com.demo.yourshoppingcart.product_details.data.model

abstract class ProductDetailsModel {

    data class DetailModel(
        val itemId: String,
        val itemName: String,
        val itemDescription: String,
        val itemImages: List<String>,
        val itemPrice: String
    )
}