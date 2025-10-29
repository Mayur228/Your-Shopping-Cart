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

    private val _viewState = MutableStateFlow(MainState())
    val viewState: StateFlow<MainState> = _viewState

    init {
        fetchCurrentUser()
    }

    private fun fetchCurrentUser() {
        viewModelScope.launch {
            _viewState.value = _viewState.value.copy(isLoading = true)
            when (val result = getUserUseCase.invoke()) {
                is Resource.Data<*> -> {
                    val user = result.value as UserModel.UserResponse
                    _viewState.value = _viewState.value.copy(isLoading = false, user = user)
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

    fun guestLogin() {
        viewModelScope.launch {
            _viewState.value = _viewState.value.copy(isLoading = true)
            when (val result = guestLoginUseCase.invoke()) {
                is Resource.Data<*> -> {
                    val guestId = result.value as String
                    _viewState.value = _viewState.value.copy(
                        isLoading = false,
                        user = UserModel.UserResponse(
                            userId = guestId,
                            userNum = "",
                            userType = USERTYPE.GUEST,
                            isLogin = true,
                            cart = null
                        )
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

    fun updateTheme(isDark: Boolean) {
        _viewState.value = _viewState.value.copy(isDark = isDark)
    }
}