package com.demo.yourshoppingcart.ui.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.yourshoppingcart.Resource
import com.demo.yourshoppingcart.payment.domain.entity.paymentEntity
import com.demo.yourshoppingcart.payment.domain.usecase.AddPaymentMethodUseCase
import com.demo.yourshoppingcart.payment.domain.usecase.DeletePaymentMethodUseCase
import com.demo.yourshoppingcart.payment.domain.usecase.GetPaymentMethodUseCase
import com.demo.yourshoppingcart.payment.domain.usecase.GetSelectedPaymentMethodUseCase
import com.demo.yourshoppingcart.payment.domain.usecase.SelectedPaymentMethodUseCase
import com.demo.yourshoppingcart.payment.domain.usecase.UpdatePaymentMethodUseCase
import com.demo.yourshoppingcart.ui.cart.CheckoutState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val addPaymentMethodUseCase: AddPaymentMethodUseCase,
    private val getPaymentMethodUseCase: GetPaymentMethodUseCase,
    private val updatePaymentMethodUseCase: UpdatePaymentMethodUseCase,
    private val deletePaymentMethodUseCase: DeletePaymentMethodUseCase,
    private val selectedPaymentMethodUseCase: SelectedPaymentMethodUseCase,
    private val getSelectedPaymentMethodUseCase: GetSelectedPaymentMethodUseCase
): ViewModel() {

    private val _viewState = MutableStateFlow<CheckoutState>(CheckoutState.Loading)
    val viewState: StateFlow<CheckoutState> = _viewState

    init {
        loadPaymentMethods()
    }

    private fun loadPaymentMethods() = viewModelScope.launch {
        _viewState.value = CheckoutState.Loading

        val paymentList = getPaymentMethodUseCase.invoke()
        val selectedMethod = getSelectedPaymentMethodUseCase.invoke()

        when {
            paymentList is Resource.Data && selectedMethod is Resource.Data -> {
                _viewState.value = CheckoutState.Success(
                    selectedPaymentMethod = selectedMethod.value,
                    typesOfPaymentMethods = paymentList.value
                )
            }

            paymentList is Resource.Error ->
                _viewState.value = CheckoutState.Error(paymentList.throwable.message ?: "Something went wrong")

            selectedMethod is Resource.Error ->
                _viewState.value = CheckoutState.Error(selectedMethod.throwable.message ?: "Something went wrong")
        }
    }

    fun selectPaymentMethod(methodId: String) {
        viewModelScope.launch {
            selectedPaymentMethodUseCase.invoke(methodId)
            loadPaymentMethods()
        }
    }

    fun addPaymentMethod(method: paymentEntity) = viewModelScope.launch {
        addPaymentMethodUseCase.invoke(method)
        loadPaymentMethods()
    }

    fun updatePaymentMethod(id: String, method: paymentEntity) = viewModelScope.launch {
        updatePaymentMethodUseCase.invoke(id, method)
        loadPaymentMethods()
    }

    fun deletePaymentMethod(id: String) = viewModelScope.launch {
        deletePaymentMethodUseCase.invoke(id)
        loadPaymentMethods()
    }
}