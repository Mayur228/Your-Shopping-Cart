package com.demo.yourshoppingcart.payment.domain.reposioty

import com.demo.yourshoppingcart.Resource
import com.demo.yourshoppingcart.payment.data.model.PaymentModel
import com.demo.yourshoppingcart.payment.domain.entity.paymentEntity

interface PaymentRepository {
    suspend fun addPaymentMethod(method: PaymentModel): Resource<String>
    suspend fun getPaymentMethod(): Resource<List<paymentEntity>>
    suspend fun updatePaymentMethod(paymentMethodId: String, method: paymentEntity): Resource<String>
    suspend fun deletePaymentMethod(paymentMethodId: String): Resource<String>
    suspend fun selectedPaymentMethod(id: String): Resource<Unit>
    suspend fun getSelectedPaymentMethod(): Resource<String>
}