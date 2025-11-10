package com.demo.yourshoppingcart.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.yourshoppingcart.Resource
import com.demo.yourshoppingcart.home.domain.entity.HomeEntity
import com.demo.yourshoppingcart.home.domain.usecase.GetAllItemUseCase
import com.demo.yourshoppingcart.home.domain.usecase.GetCategoryUseCase
import com.demo.yourshoppingcart.home.domain.usecase.GetSelectedCategoryItemUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val getCategoryUseCase: GetCategoryUseCase,
    val getAllItemUseCase: GetAllItemUseCase,
    val getSelectedCategoryItemUseCase: GetSelectedCategoryItemUseCase,
) : ViewModel() {

    private val _viewState = MutableStateFlow(HomeViewState())
    val viewState: StateFlow<HomeViewState> = _viewState

    init {
        getAllCategory()
        getAllItem()
    }

    fun getAllCategory() {
        viewModelScope.launch {
            _viewState.value = _viewState.value.copy(isLoading = true)

            val result = getCategoryUseCase()

            when (result) {
                is Resource.Data<*> -> {
                    val categoryResponse = result.value as HomeEntity.CategoryResponseEntity
                    _viewState.value = _viewState.value.copy(
                        isLoading = false,
                        categories = categoryResponse.categoryList,
                        errorMessage = null
                    )
                }

                is Resource.Error -> {
                    _viewState.value = _viewState.value.copy(
                        isLoading = false,
                        errorMessage = result.throwable.message
                    )
                }
            }
        }
    }

    fun getAllItem() {
        viewModelScope.launch {
            _viewState.value = _viewState.value.copy(isItemLoading = true)

            val result = getAllItemUseCase()
            when (result) {
                is Resource.Data<*> -> {
                    val itemResponse = result.value as HomeEntity.CategoryItemResponseEntity
                    _viewState.value = _viewState.value.copy(
                        isItemLoading = false,
                        items = itemResponse.itemList,
                        errorMessage = null
                    )
                }

                is Resource.Error -> {
                    _viewState.value = _viewState.value.copy(
                        isItemLoading = false,
                        errorMessage = result.throwable.message
                    )
                }
            }
        }
    }

    fun getSelectedCatItem(category: String) {
        viewModelScope.launch {
            _viewState.value = _viewState.value.copy(isItemLoading = true)
            val result = getSelectedCategoryItemUseCase(catId = category)
            when (result) {
                is Resource.Data<*> -> {
                    val itemResponse = result.value as HomeEntity.CategoryItemResponseEntity
                    _viewState.value = _viewState.value.copy(
                        isItemLoading = false,
                        items = itemResponse.itemList,
                        errorMessage = null
                    )
                }

                is Resource.Error -> {
                    _viewState.value = _viewState.value.copy(
                        isItemLoading = false,
                        errorMessage = result.throwable.message
                    )
                }
            }
        }
    }
}