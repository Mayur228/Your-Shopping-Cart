package com.demo.yourshoppingcart.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.yourshoppingcart.Resource
import com.demo.yourshoppingcart.user.data.model.USERTYPE
import com.demo.yourshoppingcart.user.data.model.UserModel
import com.demo.yourshoppingcart.user.domain.usecase.PhoneLoginUseCase
import com.demo.yourshoppingcart.user.domain.usecase.SendOtpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhoneLoginViewModel @Inject constructor(
    private val sendOtpUseCase: SendOtpUseCase,
    private val phoneLoginUseCase: PhoneLoginUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<PhoneLoginState>(PhoneLoginState.Idle)
    val state: StateFlow<PhoneLoginState> = _state
    private var verificationId: String? = null
    private var currentPhone: String = ""
    private var currentOtp: String = ""

    fun onEvent(event: PhoneLoginEvent) {
        when(event) {
            is PhoneLoginEvent.PhoneChanged -> {
                currentPhone = event.phone
                _state.value = PhoneLoginState.EnterPhone(currentPhone)
            }
            PhoneLoginEvent.SendOtpClicked -> sendOtp()
            is PhoneLoginEvent.OtpChanged -> {
                currentOtp = event.otp
                _state.value = PhoneLoginState.EnterOtp(currentPhone, currentOtp, otpSent = true)
            }
            PhoneLoginEvent.VerifyOtpClicked -> verifyOtp()
        }
    }

    private fun sendOtp() {
        viewModelScope.launch {
            _state.value = PhoneLoginState.Loading
            viewModelScope.launch {
                val result = sendOtpUseCase.invoke("+91$currentPhone")
                when(result) {
                    is Resource.Data<String> -> {
                        verificationId = result.value
                        _state.value = PhoneLoginState.EnterOtp(currentPhone, "", otpSent = true)
                    }
                    is Resource.Error -> {
                        _state.value = PhoneLoginState.Error(result.throwable.message ?: "Failed to send OTP")

                    }
                }
            }
        }
    }

    private fun verifyOtp() {
        viewModelScope.launch {
            _state.value = PhoneLoginState.Loading
            val result = phoneLoginUseCase.invoke(
                oldGuestId = null,
                user = UserModel.UserResponse(
                    userId = "",
                    userNum = currentPhone,
                    userType = USERTYPE.LOGGED,
                    isLogin = true,
                    cart = null
                ),
                verificationId = verificationId,
                otp = currentOtp
            )

            when(result) {
                is Resource.Data<String> -> {
                    _state.value = PhoneLoginState.Success(result.value)
                }
                is Resource.Error -> {
                    _state.value = PhoneLoginState.Error(result.throwable.message ?: "Failed to verify OTP")
                }
            }
        }
    }
}