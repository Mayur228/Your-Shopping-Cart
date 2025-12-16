package com.demo.yourshoppingcart.ui.wish_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.yourshoppingcart.Resource
import com.demo.yourshoppingcart.wish_list.data.model.WishList
import com.demo.yourshoppingcart.wish_list.domain.usecase.AddToWishListUseCase
import com.demo.yourshoppingcart.wish_list.domain.usecase.GetWishListUseCase
import com.demo.yourshoppingcart.wish_list.domain.usecase.RemoveWishListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WishListViewModel @Inject constructor(
    private val addToWishListUseCase: AddToWishListUseCase,
    private val getWishListUseCase: GetWishListUseCase,
    private val removeWishListUseCase: RemoveWishListUseCase
): ViewModel() {
    private val _state = MutableStateFlow<WishListState>(WishListState.Loading)
    val state: StateFlow<WishListState> = _state

    init {
        getWishList()
    }

    private fun getWishList() {
        _state.value = WishListState.Loading
        viewModelScope.launch {
            val result = getWishListUseCase()
            when(result) {
                is Resource.Data<List<WishList>> -> {
                    _state.value = WishListState.Success(list = result.value)
                }
                is Resource.Error -> {
                    _state.value = WishListState.Error(result.throwable.message ?: "Something went wrong")
                }
            }
        }
    }

    fun addToWishList(wishList: WishList) {
        _state.value = WishListState.Loading
        viewModelScope.launch {
            val result = addToWishListUseCase(wishList = wishList)
            when(result) {
                is Resource.Data<Unit> -> {
                    getWishList()
                }
                is Resource.Error -> {
                    _state.value = WishListState.Error(result.throwable.message ?: "Something went wrong")
                }
            }
        }
    }

    fun removeWishList(id: String) {
        _state.value = WishListState.Loading
        viewModelScope.launch {
            val result = removeWishListUseCase(id = id)
            when(result) {
                is Resource.Data<Unit> -> {
                    getWishList()
                }
                is Resource.Error -> {
                    _state.value = WishListState.Error(result.throwable.message ?: "Something went wrong")
                }
            }
        }
    }
}