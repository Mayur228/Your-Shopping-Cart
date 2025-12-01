package com.demo.yourshoppingcart.ui.profile

import com.demo.yourshoppingcart.user.data.model.UserModel

sealed class ProfileState {
    object Loading: ProfileState()
    data class Success(val userDetails: UserModel.UserResponse, val orderDetails: List<String>): ProfileState()
    data class Error(val error: String): ProfileState()
}