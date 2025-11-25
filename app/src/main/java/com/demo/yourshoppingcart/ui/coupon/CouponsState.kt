package com.demo.yourshoppingcart.ui.coupon

import com.demo.yourshoppingcart.coupon.domain.entity.couponEntity

sealed class CouponsState {
    object Loading: CouponsState()
    data class Success(val coupons: List<couponEntity>): CouponsState()
    data class Error(val error: String): CouponsState()
}
