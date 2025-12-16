package com.demo.yourshoppingcart.home.data.model

abstract class HomeModel {

    data class Category(
        val catId: String,
        val catName: String,
        val catImg: String,
    )

    data class Product(
        val productId: String,
        val productName: String,
        val productImg: String,
        val productDes: String,
        val productPrice: Int,
        val productDiscount: Int,
        val productRating: Double,
        val cat: String
    )

}