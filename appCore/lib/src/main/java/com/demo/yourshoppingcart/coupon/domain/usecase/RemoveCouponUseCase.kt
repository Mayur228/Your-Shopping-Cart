package com.demo.yourshoppingcart.coupon.domain.usecase

import com.demo.yourshoppingcart.Resource
import com.demo.yourshoppingcart.coupon.domain.entity.couponEntity
import com.demo.yourshoppingcart.coupon.domain.repository.CouponRepository
import com.demo.yourshoppingcart.payment.domain.entity.paymentEntity
import com.demo.yourshoppingcart.payment.domain.reposioty.PaymentRepository
import org.koin.core.annotation.Factory

@Factory
class RemoveCouponUseCase(private val repository: CouponRepository) {
    suspend operator fun invoke(id: String): Resource<Unit> {
        return repository.removeCoupon(id = id)
    }
}