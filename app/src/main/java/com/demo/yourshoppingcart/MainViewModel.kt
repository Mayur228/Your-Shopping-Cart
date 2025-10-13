package com.demo.yourshoppingcart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.yourshoppingcart.user.data.model.UserModel
import com.demo.yourshoppingcart.user.domain.usecase.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    //private val storageProvider: StorageProvider
    private val getUserUseCase: GetUserUseCase,
    private val addUserUseCase: GetUserUseCase
) : ViewModel() {
    private val _viewState = MutableStateFlow(MainState())
    val viewState: StateFlow<MainState> = _viewState

    init {
        getUser()
    }

    fun getUser() {
        viewModelScope.launch {
            _viewState.value = _viewState.value.copy(isLoading = true)
            val result = getUserUseCase.invoke()
            when (result) {
                is Resource.Data<*> -> {
                    val user = result as UserModel.UserResponse
                    _viewState.value = _viewState.value.copy(
                        isLoading = false,
                        user = user
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

    fun addUser(isAnonymouse: Boolean) {

    }

    fun getView(isDark: Boolean) {
        _viewState.value = _viewState.value.copy(isDark = isDark)
    }

    fun updateViewTheme(isDark: Boolean) {
        _viewState.value = _viewState.value.copy(isDark)
    }
}