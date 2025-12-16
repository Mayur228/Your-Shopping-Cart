package com.demo.yourshoppingcart.ui.wish_list

import com.demo.yourshoppingcart.wish_list.domain.entity.wishListEntity

sealed class WishListState {
    object Loading : WishListState()
    data class Success(val list: List<wishListEntity>) : WishListState()
    data class Error(val message: String) : WishListState()
}
