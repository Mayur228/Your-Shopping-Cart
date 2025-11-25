package com.demo.yourshoppingcart.coupon.domain.repository

import com.demo.yourshoppingcart.Resource
import com.demo.yourshoppingcart.coupon.domain.entity.couponEntity

interface CouponRepository {
    suspend fun getCoupon(): Resource<List<couponEntity>>
    suspend fun applyCoupon(id: String): Resource<Unit>
    suspend fun removeCoupon(id: String): Resource<Unit>
}