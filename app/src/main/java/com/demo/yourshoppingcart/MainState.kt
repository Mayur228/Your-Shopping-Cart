package com.demo.yourshoppingcart

import com.demo.yourshoppingcart.user.data.model.UserModel

sealed class MainState {
    object Loading: MainState()
    data class Success(val isDark: Boolean): MainState()
    data class Error(val error: String): MainState()
}