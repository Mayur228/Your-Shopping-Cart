package com.demo.yourshoppingcart.ui.checkout

import com.demo.yourshoppingcart.payment.domain.entity.paymentEntity

sealed class CheckoutState {
    object Loading: CheckoutState()
    data class Success(val selectedPaymentMethod: String, val typesOfPaymentMethods: List<paymentEntity>): CheckoutState()
    data class Error(val error: String): CheckoutState()
}
