package com.demo.yourshoppingcart.coupon.data.model

data class Coupon(
    val id: String = "",
    val code: String = "",
    val description: String = "",
    val discountAmount: Int = 0,
    val discountPercent: Int = 0,
    val expiryDate: String = "",
    val isApplied: Boolean = false
)