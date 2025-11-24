package com.demo.yourshoppingcart.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.yourshoppingcart.Resource
import com.demo.yourshoppingcart.home.domain.entity.HomeEntity
import com.demo.yourshoppingcart.home.domain.usecase.GetAllItemUseCase
import com.demo.yourshoppingcart.home.domain.usecase.GetCategoryUseCase
import com.demo.yourshoppingcart.home.domain.usecase.GetSelectedCategoryItemUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCategoryUseCase: GetCategoryUseCase,
    private val getAllItemUseCase: GetAllItemUseCase,
    private val getSelectedCategoryItemUseCase: GetSelectedCategoryItemUseCase
) : ViewModel() {

    private val _viewState = MutableStateFlow<HomeViewState>(HomeViewState.Loading)
    val viewState: StateFlow<HomeViewState> = _viewState

    private val _viewEvent = MutableSharedFlow<HomeEvent>()
    val viewEvent: SharedFlow<HomeEvent> = _viewEvent

    init {
        onEvent(HomeEvent.LoadHomeData)
    }
    fun onEvent(event: HomeEvent) {
        viewModelScope.launch {
            when(event) {
                HomeEvent.LoadHomeData -> loadData()
                is HomeEvent.SelectCategory -> _viewEvent.emit(event)
                is HomeEvent.NavigateToProductDetails -> _viewEvent.emit(event)
                HomeEvent.NavigateToCart -> _viewEvent.emit(event)
                is HomeEvent.ToggleChange -> _viewEvent.emit(event)
            }
        }
    }

    fun loadData() {
        viewModelScope.launch {
            _viewState.value = HomeViewState.Loading

            val categoryResult = getCategoryUseCase()
            val itemResult = getAllItemUseCase()

            if (categoryResult is Resource.Data<*> && itemResult is Resource.Data<*>) {
                val categories =
                    (categoryResult.value as HomeEntity.CategoryResponseEntity).categoryList
                val items =
                    (itemResult.value as HomeEntity.CategoryItemResponseEntity).itemList

                _viewState.value = HomeViewState.Success(
                    categories = categories,
                    items = items,
                    isLoading = false
                )
            } else {
                val errorMessage = (categoryResult as? Resource.Error)?.throwable?.message
                    ?: (itemResult as? Resource.Error)?.throwable?.message
                    ?: "Something went wrong"
                _viewState.value = HomeViewState.Error(errorMessage)
            }
        }
    }

    fun getSelectedCatItem(category: String) {
        viewModelScope.launch {
            val currentState = viewState.value
            val currentCategories = (currentState as? HomeViewState.Success)?.categories.orEmpty()

            _viewState.value = HomeViewState.Success(
                categories = currentCategories,
                items = emptyList(),
                isLoading = true
            )

            val result = getSelectedCategoryItemUseCase(catId = category)
            if (result is Resource.Data<*>) {
                val items = (result.value as HomeEntity.CategoryItemResponseEntity).itemList
                _viewState.value = HomeViewState.Success(
                    categories = currentCategories,
                    items = items,
                    isLoading = false
                )
            } else {
                val errorMessage = (result as? Resource.Error)?.throwable?.message ?: "Failed to load items"
                _viewState.value = HomeViewState.Error(errorMessage)
            }
        }
    }

}