package com.demo.yourshoppingcart.ui.coupon

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.yourshoppingcart.Resource
import com.demo.yourshoppingcart.coupon.domain.entity.couponEntity
import com.demo.yourshoppingcart.coupon.domain.usecase.ApplyCouponUseCase
import com.demo.yourshoppingcart.coupon.domain.usecase.GetCouponsUseCase
import com.demo.yourshoppingcart.coupon.domain.usecase.RemoveCouponUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CouponsViewModel @Inject constructor(
    private val getCouponsUseCase: GetCouponsUseCase,
    private val applyCouponUseCase: ApplyCouponUseCase,
    private val removeCouponUseCase: RemoveCouponUseCase
) :
    ViewModel() {
    private val _viewState = MutableStateFlow<CouponsState>(CouponsState.Loading)
    val viewState: StateFlow<CouponsState> = _viewState

    init {
        loadCoupons()
    }

    private fun loadCoupons() {
        viewModelScope.launch {
            _viewState.value = CouponsState.Loading
            val result = getCouponsUseCase()
            when (result) {
                is Resource.Data<List<couponEntity>> -> {
                    _viewState.value = CouponsState.Success(coupons = result.value)
                }

                is Resource.Error -> {
                    _viewState.value =
                        CouponsState.Error(result.throwable.message ?: "something went wrong")
                }
            }
        }
    }

    fun applyCoupon(id: String) {
        viewModelScope.launch {
            _viewState.value = CouponsState.Loading
            val result = applyCouponUseCase(id)
            when (result) {
                is Resource.Data<Unit> -> {
                    loadCoupons()
                }

                is Resource.Error -> {
                    _viewState.value =
                        CouponsState.Error(result.throwable.message ?: "something went wrong")
                }
            }
        }
    }

    fun removeCoupon(id: String) {
        viewModelScope.launch {
            _viewState.value = CouponsState.Loading
            val result = removeCouponUseCase(id)
            when (result) {
                is Resource.Data<Unit> -> {
                    loadCoupons()
                }

                is Resource.Error -> {
                    _viewState.value =
                        CouponsState.Error(result.throwable.message ?: "something went wrong")
                }
            }
        }
    }
}