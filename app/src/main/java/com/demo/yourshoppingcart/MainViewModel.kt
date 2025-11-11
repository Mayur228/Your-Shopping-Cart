package com.demo.yourshoppingcart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.yourshoppingcart.user.data.model.USERTYPE
import com.demo.yourshoppingcart.user.data.model.UserModel
import com.demo.yourshoppingcart.user.domain.usecase.GetUserUseCase
import com.demo.yourshoppingcart.user.domain.usecase.GuestLoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val guestLoginUseCase: GuestLoginUseCase
) : ViewModel() {

    private val _viewState = MutableStateFlow<MainState>(MainState.Loading)
    val viewState: StateFlow<MainState> = _viewState

    init {
        fetchCurrentUser()
    }

    private fun fetchCurrentUser() {
        viewModelScope.launch {
            _viewState.value = MainState.Loading
            when (val result = getUserUseCase.invoke()) {
                is Resource.Data<UserModel.UserResponse> -> {
                    _viewState.value = MainState.Success(isDark = (_viewState.value as MainState.Success).isDark,user = result.value)
                }
                is Resource.Error -> {
                    guestLogin()
                }
            }
        }
    }

    fun guestLogin() {
        viewModelScope.launch {
            _viewState.value = MainState.Loading
            when (val result = guestLoginUseCase.invoke()) {
                is Resource.Data<String> -> {
                    val guestUser = UserModel.UserResponse(
                        userId = result.value,
                        userNum = "",
                        userType = USERTYPE.GUEST,
                        isLogin = true,
                        cart = null
                    )
                    _viewState.value = MainState.Success((_viewState.value as MainState.Success).isDark,user = guestUser)
                }
                is Resource.Error -> {
                    _viewState.value = MainState.Error(
                        result.throwable.message ?: "Something went wrong"
                    )
                }
            }
        }
    }

    fun updateTheme(isDark: Boolean) {
        val currentUser = (_viewState.value as? MainState.Success)?.user
        _viewState.value = MainState.Success(isDark = isDark, user = currentUser)
    }
}