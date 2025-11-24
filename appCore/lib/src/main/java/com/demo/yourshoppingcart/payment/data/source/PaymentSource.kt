package com.demo.yourshoppingcart.payment.data.source

import com.demo.yourshoppingcart.Resource
import com.demo.yourshoppingcart.common.network.DocumentApi
import com.demo.yourshoppingcart.payment.data.model.PaymentModel
import org.koin.core.annotation.Factory

interface PaymentSource {
    suspend fun addPaymentMethod(method: PaymentModel): Resource<String>
    suspend fun getPaymentMethod(): Resource<List<PaymentModel>>
    suspend fun updatePaymentMethod(paymentMethodId: String, method: PaymentModel): Resource<String>
    suspend fun deletePaymentMethod(paymentMethodId: String): Resource<String>
    suspend fun selectedPaymentMethod(id: String): Resource<Unit>
    suspend fun getSelectedPaymentMethod(): Resource<String>
}

@Factory
class PaymentSourceImpl(private val api: DocumentApi) : PaymentSource {
    override suspend fun addPaymentMethod(method: PaymentModel): Resource<String> {
        return try {
            val result = api.addPaymentMethod(method = method)
            Resource.Data(result)
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    override suspend fun getPaymentMethod(): Resource<List<PaymentModel>> {
        return try {
            val result = api.getPaymentMethod()
            Resource.Data(result)
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    override suspend fun updatePaymentMethod(
        paymentMethodId: String,
        method: PaymentModel
    ): Resource<String> {
        return try {
            val result =
                api.updatePaymentMethod(paymentMethodId = paymentMethodId, updatedMethod = method)
            Resource.Data(result)
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    override suspend fun deletePaymentMethod(paymentMethodId: String): Resource<String> {
        return try {
            val result = api.deletePaymentMethod(paymentMethodId = paymentMethodId)
            Resource.Data(result)
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    override suspend fun selectedPaymentMethod(id: String): Resource<Unit> {
        return try {
            val result = api.selectedPaymentMethod(id = id)
            Resource.Data(result)
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }

    override suspend fun getSelectedPaymentMethod(): Resource<String> {
        return try {
            val result = api.getSelectedPaymentMethod()
            Resource.Data(result)
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }
}