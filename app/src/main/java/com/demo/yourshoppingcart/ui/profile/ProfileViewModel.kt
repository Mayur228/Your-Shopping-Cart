package com.demo.yourshoppingcart.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.yourshoppingcart.Resource
import com.demo.yourshoppingcart.payment.domain.entity.paymentEntity
import com.demo.yourshoppingcart.user.data.model.AddressModel
import com.demo.yourshoppingcart.user.data.model.UserModel
import com.demo.yourshoppingcart.user.domain.usecase.GetUserUseCase
import com.demo.yourshoppingcart.user.domain.usecase.UpdateUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<ProfileState>(ProfileState.Loading)
    val state: StateFlow<ProfileState> = _state

    init {
        loadUser()
    }

    private fun loadUser() {
        viewModelScope.launch {
            _state.value = ProfileState.Loading
            when (val result = getUserUseCase.invoke()) {
                is Resource.Data -> {
                    _state.value = ProfileState.Success(
                        userDetails = result.value,
                        orderDetails = emptyList()
                    )
                }

                is Resource.Error -> {
                    _state.value = ProfileState.Error(result.throwable.message ?: "Something went wrong!")
                }
            }
        }
    }

    fun updateUser(user: UserModel.UserResponse) {
        viewModelScope.launch {
            val result = updateUserUseCase.invoke(id = user.userId, user = user)
            when (result) {
                is Resource.Data -> loadUser()
                is Resource.Error -> _state.value =
                    ProfileState.Error(result.throwable.message ?: "Something went wrong")
            }        }
    }

    fun addAddress(address: AddressModel) {
        val currentState = _state.value as? ProfileState.Success ?: return
        val currentUser = currentState.userDetails

        val updatedAddressList = currentUser.address.toMutableList().apply {
            add(address)
        }

        val updatedUser = currentUser.copy(address = updatedAddressList)

        viewModelScope.launch {
            _state.value = ProfileState.Loading
            val result = updateUserUseCase.invoke(id = updatedUser.userId, user = updatedUser)
            when (result) {
                is Resource.Data -> loadUser()
                is Resource.Error -> _state.value =
                    ProfileState.Error(result.throwable.message ?: "Something went wrong")
            }
        }
    }

    fun updateAddress(id: String, newAddress: AddressModel) {
        val currentState = _state.value as? ProfileState.Success ?: return
        val currentUser = currentState.userDetails

        val updatedAddressList = currentUser.address.map {
            if (it.id == id) newAddress else it
        }

        val updatedUser = currentUser.copy(address = updatedAddressList)

        viewModelScope.launch {
            _state.value = ProfileState.Loading

            val result = updateUserUseCase.invoke(id = updatedUser.userId, user = updatedUser)
            when (result) {
                is Resource.Data -> loadUser()
                is Resource.Error -> _state.value =
                    ProfileState.Error(result.throwable.message ?: "Something went wrong")
            }
        }
    }

    fun deleteAddress(id: String) {
        val currentState = _state.value as? ProfileState.Success ?: return
        val currentUser = currentState.userDetails

        val updatedList = currentUser.address.filter { it.id != id }

        val updatedUser = currentUser.copy(address = updatedList)

        viewModelScope.launch {
            _state.value = ProfileState.Loading

            val result = updateUserUseCase.invoke(id = updatedUser.userId, user = updatedUser)
            when (result) {
                is Resource.Data -> loadUser()
                is Resource.Error -> _state.value =
                    ProfileState.Error(result.throwable.message ?: "Something went wrong")
            }
        }
    }

    fun selectPaymentMethod(methodId: String) {
        val currentState = _state.value as? ProfileState.Success ?: return
        val currentUser = currentState.userDetails

        val updatedUser = currentUser.copy(selectedPaymentMethod = methodId)

        viewModelScope.launch {
            _state.value = ProfileState.Loading

            val result = updateUserUseCase.invoke(id = updatedUser.userId, user = updatedUser)
            when (result) {
                is Resource.Data -> loadUser()
                is Resource.Error -> _state.value =
                    ProfileState.Error(result.throwable.message ?: "Something went wrong")
            }
        }
    }

    fun addPaymentMethod(method: paymentEntity) {
        val currentState = _state.value as? ProfileState.Success ?: return
        val currentUser = currentState.userDetails

        val updatedAddressList = currentUser.paymentMethods.toMutableList().apply {
            add(method)
        }

        val updatedUser = currentUser.copy(paymentMethods = updatedAddressList)

        viewModelScope.launch {
            _state.value = ProfileState.Loading
            val result = updateUserUseCase.invoke(id = currentUser.userId, user = updatedUser)

            when (result) {
                is Resource.Data -> loadUser()
                is Resource.Error -> _state.value =
                    ProfileState.Error(result.throwable.message ?: "Something went wrong")
            }
        }
    }

    fun updatePaymentMethod(id: String, method: paymentEntity) {
        val currentState = _state.value as? ProfileState.Success ?: return
        val currentUser = currentState.userDetails

        val updatedPaymentMethodList = currentUser.paymentMethods.map {
            if (it.id == id) method else it
        }

        val updatedUser = currentUser.copy(paymentMethods = updatedPaymentMethodList)

        viewModelScope.launch {
            _state.value = ProfileState.Loading

            val result = updateUserUseCase.invoke(id = updatedUser.userId, user = updatedUser)
            when (result) {
                is Resource.Data -> loadUser()
                is Resource.Error -> _state.value =
                    ProfileState.Error(result.throwable.message ?: "Something went wrong")
            }
        }
    }

    fun deletePaymentMethod(id: String) {
        val currentState = _state.value as? ProfileState.Success ?: return
        val currentUser = currentState.userDetails

        val updatedList = currentUser.paymentMethods.filter { it.id != id }

        val updatedUser = currentUser.copy(paymentMethods = updatedList)

        viewModelScope.launch {
            _state.value = ProfileState.Loading

            val result = updateUserUseCase.invoke(id = updatedUser.userId, user = updatedUser)
            when (result) {
                is Resource.Data -> loadUser()
                is Resource.Error -> _state.value =
                    ProfileState.Error(result.throwable.message ?: "Something went wrong")
            }
        }
    }
}