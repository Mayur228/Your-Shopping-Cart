package com.demo.yourshoppingcart.ui.order_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.yourshoppingcart.Resource
import com.demo.yourshoppingcart.order.domain.entity.orderEntity
import com.demo.yourshoppingcart.order.domain.usecase.GetOrderDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderDetailsViewModel @Inject constructor(
    private val getOrderDetailsUseCase: GetOrderDetailsUseCase
): ViewModel()  {
    private val _state = MutableStateFlow<OrderDetailsState>(OrderDetailsState.Loading)
    val state: StateFlow<OrderDetailsState> = _state

    fun getOrderDetails(id: String) {
        viewModelScope.launch {
            _state.value = OrderDetailsState.Loading
            val result = getOrderDetailsUseCase.invoke(id = id)
            when(result){
                is Resource.Data<orderEntity> -> {
                    _state.value = OrderDetailsState.Success(order = result.value)
                }
                is Resource.Error -> {
                    _state.value = OrderDetailsState.Error(error = result.throwable.message ?: "something went wrong")
                }
            }
        }
    }
}