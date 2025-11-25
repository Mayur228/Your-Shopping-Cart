package com.demo.yourshoppingcart.coupon.data.repository

import com.demo.yourshoppingcart.Resource
import com.demo.yourshoppingcart.coupon.data.source.CouponSource
import com.demo.yourshoppingcart.coupon.domain.entity.couponEntity
import com.demo.yourshoppingcart.coupon.domain.repository.CouponRepository
import org.koin.core.annotation.Factory

@Factory
class CouponRepositoryImpl(private val source: CouponSource): CouponRepository {
    override suspend fun getCoupon(): Resource<List<couponEntity>> {
        return source.getCoupons()
    }

    override suspend fun applyCoupon(id: String): Resource<Unit> {
        return source.applyCoupon(id = id)
    }

    override suspend fun removeCoupon(id: String): Resource<Unit> {
        return source.removeCoupon(id = id)
    }

}