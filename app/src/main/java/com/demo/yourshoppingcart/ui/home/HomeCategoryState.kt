package com.demo.yourshoppingcart.ui.home

import com.demo.yourshoppingcart.home.domain.entity.HomeEntity

data class HomeViewState(
    val isLoading: Boolean = false,
    val isItemLoading: Boolean = false,
    val categories: List<HomeEntity.CategoryEntity> = emptyList(),
    val items: List<HomeEntity.ItemEntity> = emptyList(),
    val errorMessage: String? = null
)
