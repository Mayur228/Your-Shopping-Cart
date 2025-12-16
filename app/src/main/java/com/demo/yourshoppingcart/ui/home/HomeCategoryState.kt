package com.demo.yourshoppingcart.ui.home

import com.demo.yourshoppingcart.home.domain.entity.categoryEntity
import com.demo.yourshoppingcart.home.domain.entity.productEntity


sealed class HomeViewState {
    object Loading : HomeViewState()
    data class Success(
        val categories: List<categoryEntity>,
        val items: List<productEntity>,
        val isLoading: Boolean
    ) : HomeViewState()
    data class Error(val message: String) : HomeViewState()
}

sealed class HomeEvent {
    object LoadHomeData: HomeEvent()
    data class SelectCategory(val categoryId: String) : HomeEvent()
    data class NavigateToProductDetails(val productId: String): HomeEvent()
    object NavigateToCart: HomeEvent()
    data class ToggleChange(val isDark: Boolean): HomeEvent()
}
