package com.demo.yourshoppingcart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.yourshoppingcart.user.domain.usecase.CheckUserUseCase
import com.demo.yourshoppingcart.user.domain.usecase.GuestLoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val checkUserUseCase: CheckUserUseCase,
    private val guestLoginUseCase: GuestLoginUseCase
) : ViewModel() {

    private val _viewState = MutableStateFlow<MainState>(MainState.Loading)
    val viewState: StateFlow<MainState> = _viewState

    init {
        fetchCurrentUser()
    }

    private fun currentTheme(): Boolean {
        return (viewState.value as? MainState.Success)?.isDark ?: false
    }

    private fun fetchCurrentUser() {
        viewModelScope.launch {
            _viewState.value = MainState.Loading

            when (val result = checkUserUseCase()) {
                is Resource.Data<Boolean> -> {
                    if (result.value) {
                        _viewState.value = MainState.Success(isDark = currentTheme())
                    } else {
                        guestLogin()
                    }
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

            when (guestLoginUseCase()) {
                is Resource.Data<String> -> {
                    _viewState.value = MainState.Success(isDark = currentTheme())
                }

                is Resource.Error -> {
                    _viewState.value = MainState.Error("Something went wrong")
                }
            }
        }
    }

    fun updateTheme(isDark: Boolean) {
        _viewState.value = MainState.Success(isDark = isDark)
    }
}