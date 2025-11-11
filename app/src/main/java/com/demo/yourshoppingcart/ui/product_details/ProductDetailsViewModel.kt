package com.demo.yourshoppingcart.ui.product_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.yourshoppingcart.Resource
import com.demo.yourshoppingcart.product_details.domain.entity.detailsEntity
import com.demo.yourshoppingcart.product_details.domain.usecase.GetProductDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(private val getProductDetailsUseCase: GetProductDetailsUseCase): ViewModel() {

    private val _viewState = MutableStateFlow<ProductDetailsState>(ProductDetailsState.Loading)
    val viewState: StateFlow<ProductDetailsState> = _viewState

    fun getItemDetails(itemId: String) {
        viewModelScope.launch {

            val result = getProductDetailsUseCase(itemId = itemId)

            when (result) {
                is Resource.Data<detailsEntity> -> {
                    _viewState.value = ProductDetailsState.Success(product = result.value)
                }

                is Resource.Error -> {
                    _viewState.value = ProductDetailsState.Error(error = result.throwable.message ?: "Something went wrong")
                }
            }
        }
    }
}