package com.demo.yourshoppingcart.payment.data.repository

import com.demo.yourshoppingcart.Resource
import com.demo.yourshoppingcart.payment.data.model.PaymentModel
import com.demo.yourshoppingcart.payment.data.source.PaymentSource
import com.demo.yourshoppingcart.payment.domain.entity.paymentEntity
import com.demo.yourshoppingcart.payment.domain.reposioty.PaymentRepository
import org.koin.core.annotation.Factory

@Factory
class PaymentRepositoryImpl(private val source: PaymentSource): PaymentRepository {
    override suspend fun addPaymentMethod(method: PaymentModel): Resource<String> {
        return source.addPaymentMethod(method = method)
    }

    override suspend fun getPaymentMethod(): Resource<List<paymentEntity>> {
        return source.getPaymentMethod()
    }

    override suspend fun updatePaymentMethod(
        paymentMethodId: String,
        method: paymentEntity
    ): Resource<String> {
        return source.updatePaymentMethod(paymentMethodId = paymentMethodId, method = method)
    }

    override suspend fun deletePaymentMethod(paymentMethodId: String): Resource<String> {
        return source.deletePaymentMethod(paymentMethodId = paymentMethodId)
    }

    override suspend fun selectedPaymentMethod(id: String): Resource<Unit> {
        return source.selectedPaymentMethod(id)
    }

    override suspend fun getSelectedPaymentMethod(): Resource<String> {
        return source.getSelectedPaymentMethod()
    }
}