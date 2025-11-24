package com.demo.yourshoppingcart.payment.domain.usecase

import com.demo.yourshoppingcart.Resource
import com.demo.yourshoppingcart.payment.domain.entity.paymentEntity
import com.demo.yourshoppingcart.payment.domain.reposioty.PaymentRepository
import org.koin.core.annotation.Factory

@Factory
class DeletePaymentMethodUseCase(private val repository: PaymentRepository) {
    suspend operator fun invoke(id: String): Resource<String> {
        return repository.deletePaymentMethod(paymentMethodId = id)
    }
}