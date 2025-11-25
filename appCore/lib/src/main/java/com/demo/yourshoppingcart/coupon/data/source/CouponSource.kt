package com.demo.yourshoppingcart.coupon.data.source

import com.demo.yourshoppingcart.Resource
import com.demo.yourshoppingcart.common.network.DocumentApi
import com.demo.yourshoppingcart.coupon.data.model.Coupon
import org.koin.core.annotation.Factory

interface CouponSource {
    suspend fun getCoupons(): Resource<List<Coupon>>
    suspend fun applyCoupon(id: String): Resource<Unit>
    suspend fun removeCoupon(id: String): Resource<Unit>
}

@Factory
class CouponSourceImpl(private val api: DocumentApi): CouponSource{
    override suspend fun getCoupons(): Resource<List<Coupon>> {
        return try {
            val result = api.getCoupons()
            Resource.Data(result)
        }catch (e: Exception) {
            Resource.Error(e)
        }
    }

    override suspend fun applyCoupon(id: String): Resource<Unit> {
        return try {
            val result = api.applyCoupon(id = id)
            Resource.Data(result)
        }catch (e: Exception) {
            Resource.Error(e)
        }
    }

    override suspend fun removeCoupon(id: String): Resource<Unit> {
        return try {
            val result = api.removeCoupon(id = id)
            Resource.Data(result)
        }catch (e: Exception) {
            Resource.Error(e)
        }
    }
}