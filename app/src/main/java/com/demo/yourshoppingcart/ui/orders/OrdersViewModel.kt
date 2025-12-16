package com.demo.yourshoppingcart.ui.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.yourshoppingcart.Resource
import com.demo.yourshoppingcart.order.domain.entity.orderEntity
import com.demo.yourshoppingcart.order.domain.usecase.AddOrderHistoryUseCase
import com.demo.yourshoppingcart.order_details.domain.usecase.GetOrderDetailsUseCase
import com.demo.yourshoppingcart.order.domain.usecase.GetOrderHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val addHistoryUseCase: AddOrderHistoryUseCase,
    private val getOrderHistoryUseCase: GetOrderHistoryUseCase,
): ViewModel() {
    private val _state = MutableStateFlow<OrdersState>(OrdersState.Loading)
    val state: StateFlow<OrdersState> = _state

    init {
        getOrders()
    }

    private fun getOrders() {
        viewModelScope.launch {
            _state.value = OrdersState.Loading
            val result = getOrderHistoryUseCase.invoke()
            when(result){
                is Resource.Data<List<orderEntity>> -> {
                    _state.value = OrdersState.Success(orderList = result.value)
                }
                is Resource.Error -> {
                    _state.value = OrdersState.Error(error = result.throwable.message ?: "something went wrong")
                }
            }
        }
    }

     fun addOrderHistory(order: orderEntity) {
        viewModelScope.launch {
            _state.value = OrdersState.Loading
            val result = addHistoryUseCase.invoke(order = order)
            when(result){
                is Resource.Data<Unit> -> {
                    getOrders()
                }
                is Resource.Error -> {
                    _state.value = OrdersState.Error(error = result.throwable.message ?: "something went wrong")
                }
            }
        }
    }
}