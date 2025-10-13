package com.demo.yourshoppingcart

import com.demo.yourshoppingcart.user.data.model.UserModel

data class MainState(
    val isLoading: Boolean = false,
    val isDark: Boolean = false,
    val user: UserModel.UserResponse? = null,
    val errorMessage: String? = null
)