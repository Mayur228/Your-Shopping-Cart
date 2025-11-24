package com.demo.yourshoppingcart.payment.domain.usecase

import com.demo.yourshoppingcart.Resource
import com.demo.yourshoppingcart.payment.domain.entity.paymentEntity
import com.demo.yourshoppingcart.payment.domain.reposioty.PaymentRepository
import org.koin.core.annotation.Factory

@Factory
class GetSelectedPaymentMethodUseCase(private val repository: PaymentRepository) {
    suspend operator fun invoke(): Resource<String> {
        return repository.getSelectedPaymentMethod()
    }
}