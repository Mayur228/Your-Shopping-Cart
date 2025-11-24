package com.demo.yourshoppingcart.payment.domain.usecase

import com.demo.yourshoppingcart.Resource
import com.demo.yourshoppingcart.payment.domain.entity.paymentEntity
import com.demo.yourshoppingcart.payment.domain.reposioty.PaymentRepository
import org.koin.core.annotation.Factory

@Factory
class UpdatePaymentMethodUseCase(private val repository: PaymentRepository) {
    suspend operator fun invoke(id: String, method: paymentEntity): Resource<String> {
        return repository.updatePaymentMethod(paymentMethodId = id,method = method)
    }
}